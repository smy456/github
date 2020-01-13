package com.fh.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.model.User;
import com.fh.param.UserSearchParam;
import com.fh.service.user.UserService;
import com.fh.util.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user/")
public class UserController {
    @Resource
    private UserService userService;
    private User user;

    /*    @RequestMapping("checkUserByName")
        @ResponseBody
        public ServerResponse checkUserByName( String userName){

            return userService.getUserByName(userName);
        }
        @RequestMapping("queryList")
        @ResponseBody
        public ServerResponse queryList(){
            return userService.queryList();
        }*/
    @RequestMapping("index")
    public String index(){
        return "user/list";
    }
    @RequestMapping("checkUserByName")
    @ResponseBody
    @Ignore
    public Map checkUserByName( String userName){
         Map map=new HashMap();
        map.put("valid",userService.getUserByName(userName));
        return map;
    }
    @RequestMapping("queryList")
    @ResponseBody
    public ServerResponse queryList(UserSearchParam userSearchParam){

        return userService.queryList(userSearchParam);
    }

    @RequestMapping("addUser")
    @ResponseBody
    @Ignore
    public ServerResponse addUser(User user, @RequestParam("roleArr[]") List<Integer> roleArr){

        return userService.addUser(user,roleArr);
    }

    @RequestMapping("registerUser")
    @ResponseBody
    @Ignore
    public ServerResponse registerUser(User user){

        return userService.registerUser(user);
    }

    @RequestMapping("login")
    @ResponseBody
    @Ignore
    public ServerResponse login(User user, HttpServletRequest request, HttpServletResponse response){
        return userService.login(user,request,response);
    }
    @RequestMapping("deleteUser")
    @ResponseBody
    public ServerResponse deleteUser(Integer id){

        return userService.deleteUser(id);
    }
    @RequestMapping("loginOut")
    public String loginOut(HttpServletRequest request, HttpServletResponse response){
        userService.loginOut( request,  response);
        return "redirect:/login.jsp";
    }
    //刷新缓存
    @RequestMapping("refresh")
    @ResponseBody
    public  String refresh(HttpServletRequest request){
       userService.refresh(request);
        return "redirect:/index.jsp";
    }
  // 发送验证码
   @RequestMapping("forgetCode")
   @ResponseBody
   @Ignore
   public  ServerResponse forgetCode(String phone, HttpServletRequest request){

       return ServerResponse.success(userService.forgetCode(phone,request)) ;
   }

    // 发送验证码
    @RequestMapping("confirmPw")
    @ResponseBody
    @Ignore
    public  ServerResponse confirmPw( User user ,String code, HttpServletRequest request){

        return userService.confirmPw(user,code,request) ;
    }
}
