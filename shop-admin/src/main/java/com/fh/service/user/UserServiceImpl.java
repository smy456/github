package com.fh.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.mapper.ResourceMapper;
import com.fh.mapper.RoleMapper;
import com.fh.mapper.UserMapper;
import com.fh.mapper.UserRoleMapper;
import com.fh.model.Resource;
import com.fh.model.User;
import com.fh.model.UserRole;
import com.fh.param.UserSearchParam;
import com.fh.util.Md5Util;
import com.fh.util.SendMsgUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ResourceMapper resourceMapper;
   /* @Override
    public ServerResponse getUserByName(String userName) {
        int a=0;
        User user =  userMapper.getUserByName(userName);
        if(user ==null){
            //用户不存在   可以注册
            a=1;
        }else{
            //用户已存在   不可以注册
            a=2;
        }
        return ServerResponse.success(a);
    }*/
    @Override
    public boolean getUserByName(String userName) {

        User user =  userMapper.getUserByName(userName);
        if(user ==null){
            //用户不存在   可以注册
           return true;
        }else
            //用户已存在   不可以注册
            return false;
    }


    @Override
    public ServerResponse queryList(UserSearchParam userSearchParam) {
        //查询总条数
        long totalCount = userMapper.queryCount(userSearchParam);
        List<User> list =  userMapper.queryList(userSearchParam);
        Map map = new HashMap();
        map.put("draw",userSearchParam.getDraw());
        map.put("recordsTotal",totalCount);
        map.put("recordsFiltered",totalCount);
        map.put("data",list);

        return ServerResponse.success(map);
    }

    @Override
    public ServerResponse addUser(User user,List<Integer> roleArr) {
       String salt =  RandomStringUtils.randomAlphanumeric(20);
       String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+salt))  ;
       user.setPassWord(encodePassWord);
        user.setSalt(salt);
       // userMapper.addUser( user);
        userMapper.insert(user);
        //增加用户 绑定角色
        if(roleArr!=null && roleArr.size()>0){
            roleArr.forEach(x->{
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(x);
                userRoleMapper.insert(userRole);
            });


        }


        return ServerResponse.success();
    }
    public void writeResource(Integer userId, HttpServletRequest request) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        List<Resource> list = resourceMapper.selectList(queryWrapper);

        List<Resource> currentUserResource = resourceMapper.getCurrentUserResource(userId);

        request.getSession().setAttribute(SystemConstant.SESSION_ALL_RESOURCE_LIST,list);
        request.getSession().setAttribute(SystemConstant.SESSION_RESOURCE_BY_USER_ID,currentUserResource);
    }

    @Override
    public void refresh(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
       /* writeResource(request,user);*/

    }

    @Override
    public  ServerResponse forgetCode(String phone ,HttpServletRequest request) {

            Integer flag= null;
        if(StringUtils.isNotBlank(phone) ) {
            //发送验证码
            String code = SendMsgUtil.sendCode(phone);
            if (StringUtils.isNotBlank(code)) {
                //放到session里
                request.getSession().setAttribute(SystemConstant.CODE, code);
                //验证成功
                flag = 1;
            }else {
                //验证失败
                flag = 2;
            }
        }else {
            //手机号为空
            flag = 3;
        }

        return ServerResponse.success(flag);
    }

    @Override
    public ServerResponse confirmPw(User user, String code, HttpServletRequest request) {
        //首先验证码是否正确
        String sessionCode = (String) request.getSession().getAttribute(code);
        if(StringUtils.isNotBlank(code) && code.equals(sessionCode)){
         //根据手机号 找到用户
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("user",user.getPhone());
            String salt =  RandomStringUtils.randomAlphanumeric(20);
            String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+salt))  ;
            user.setPassWord(encodePassWord);
            user.setSalt(salt);
            userMapper.update(user,queryWrapper);
            return ServerResponse.success();

        }else {

            return ServerResponse.code_error();
        }


    }


    @Override
    public ServerResponse login(User user, HttpServletRequest request, HttpServletResponse response) {
        int flag=0;
        User us =  userMapper.getUserByName(user.getUserName());
        if(us==null){
            //用户不存在
            flag= SystemConstant.LOGGIN_USERNAME_ERROR;
        }else{
           String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+us.getSalt()));
            if(!us.getPassWord().equals(encodePassWord)){
                //用户密码错误
                flag=SystemConstant.LOGGIN_PASSWORD_ERROR;
            }else{
                //登陆成功
                flag=SystemConstant.LOGGIN_SUCCESS;
                //把用户信息放到session中
                request.getSession().setAttribute(SystemConstant.LOGGIN_CURRENT_USER,us);

                //记住我 一周
                if(user.getRememberMe()==1){
                    writeCookie(user, response);

                }


            }
        }
        return ServerResponse.success(flag);
    }

    @Override
    public ServerResponse deleteUser(Integer id) {
        userMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    public void loginOut(HttpServletRequest request, HttpServletResponse response) {
        //清除session中所有数据
        request.getSession().invalidate();
        //清除cookie
        cleanCookie(response);
    }

    @Override
    public ServerResponse registerUser(User user) {
        String salt =  RandomStringUtils.randomAlphanumeric(20);
        String encodePassWord =  Md5Util.md5(Md5Util.md5(user.getPassWord()+salt))  ;
        user.setPassWord(encodePassWord);
        user.setSalt(salt);
         userMapper.addUser( user);
        //userMapper.insert(user);
        return ServerResponse.success();
    }

    private void cleanCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(SystemConstant.COOKIE_KEY,"");
        //设置cookie过期时间 单位是秒
        cookie.setMaxAge(0);
        //cookie.setMaxAge(1*60);
        //这种cookie作用域
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void writeCookie(User user, HttpServletResponse response) {
        //把用户信息存储到cookie中
        try {
        String encodeUserName = URLEncoder.encode(user.getUserName(),"UTF-8");
        Cookie cookie = new Cookie(SystemConstant.COOKIE_KEY,encodeUserName);
        //设置cookie过期时间 单位是秒
        cookie.setMaxAge(SystemConstant.COOKIE_OUT_TIME);
        //cookie.setMaxAge(1*60);
        //这种cookie作用域
        cookie.setPath("/");
        response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
