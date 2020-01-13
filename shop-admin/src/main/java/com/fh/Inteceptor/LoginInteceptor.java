package com.fh.Inteceptor;

import com.fh.common.Ignore;
import com.fh.mapper.UserMapper;
import com.fh.model.User;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInteceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       System.out.println("-------------");
        User user = (User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法是否有自定义的注解  有注解的方法是要放开的方法
        if(method.isAnnotationPresent(Ignore.class)){
            return true;
        }
        if(user==null){
            //判断cookie中是否存在登陆信息
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if(cookie.getName().equals(SystemConstant.COOKIE_KEY)){
                    //把用户信息 重修放到session中
                    String decodeUserName = URLDecoder.decode(cookie.getValue(),"UTF-8");
                    User us =  userMapper.getUserByName(decodeUserName);
                    request.getSession().setAttribute(SystemConstant.LOGGIN_CURRENT_USER,us);
                    return true;
                }
                
            }

            //判断是否是ajax请求
            String requestHeader= request.getHeader("X-Requested-With");
            if(StringUtils.isNotBlank(requestHeader)&& requestHeader.equals("XMLHttpRequest")){
                response.setHeader("ajaxTime",SystemConstant.AJAX_SESSION_OUT);
                return  false;
            }


            //用户没有登陆  跳转到登陆页面
            response.sendRedirect(SystemConstant.LOGIN_PAGE);
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
