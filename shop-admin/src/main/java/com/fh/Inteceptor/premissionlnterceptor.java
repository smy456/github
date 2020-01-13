package com.fh.Inteceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.Ignore;
import com.fh.mapper.ResourceMapper;
import com.fh.model.Resource;
import com.fh.model.User;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Action;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.List;

public class premissionlnterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ResourceMapper resourceMapper;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("===========权限拦截=============");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断该方法是否是自定义注解 有注解的方法是放开的方法
        if(method.isAnnotationPresent(Ignore.class)){

            return true;
        }
        // method.isAnnotationPresent();

        //获取当前请求路径
        String requestURI=request.getRequestURI();
        //获取当前用户所拥有的资源
        User user = (User) request.getSession().getAttribute(SystemConstant.LOGGIN_CURRENT_USER);
        List<Resource> resourceList = resourceMapper.getCurrentUserResource(user.getId());
           boolean isFilter=false;
        //获取所有资源
        QueryWrapper<Resource> queryWrapper=new QueryWrapper<>();
        List<Resource> allResourceList1 = resourceMapper.selectList(queryWrapper);
        if(allResourceList1!=null && allResourceList1.size()>0){
            for (Resource resource : allResourceList1) {
                if(StringUtils.isNotBlank(resource.getUrl()) && requestURI.contains(resource.getUrl())){
                    isFilter=true;
                    break;

                }
            }

        }
      if(! isFilter){
          return true;
      }
        //获取所有资源
        boolean hasPremission =false;
      //判断当前请求路径。是否用户拥有的资源里面
        if(resourceList !=null && resourceList.size()>0){

            for (Resource resource : resourceList) {
                if(StringUtils.isNotBlank(resource.getUrl()) && requestURI.contains(resource.getUrl())){
                    //用户拥有当前请求的资源
                    hasPremission=true;
                    break;
              }
            }

        }
      if(!hasPremission){
          response.sendRedirect(SystemConstant.NO_PERMISSION_PAGE);
      }

        return hasPremission;
    }
}
