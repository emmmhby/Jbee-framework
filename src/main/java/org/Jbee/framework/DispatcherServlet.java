package org.Jbee.framework;

import org.Jbee.framework.bean.Data;
import org.Jbee.framework.helper.ConfigHelper;
import org.Jbee.framework.util.*;
import org.Jbee.framework.bean.Handler;
import org.Jbee.framework.bean.Param;
import org.Jbee.framework.bean.View;
import org.Jbee.framework.helper.BeanHelper;
import org.Jbee.framework.helper.ControllerHelper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig)throws ServletException{
        //初始化helper类
        HelperLoad.init();
        //获取ServletContext对象 注册servlet
        ServletContext servletContext=servletConfig.getServletContext();
        //注册处理Jsp的Servlet
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的servlet
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppStaticPath()+"*");
    }

    @Override
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)throws ServletException,IOException{
        //获取请求方法与请求路径
        String requestMethod=httpServletRequest.getMethod();
        String requestPath=httpServletRequest.getPathInfo();
        //获取Action
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler!=null){
            //获取controller类及其bean实例
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            //创建请求参数对象
            Map<String,Object> paramMap=new HashMap<String,Object>();
            Enumeration<String> paramNames=httpServletRequest.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName=paramNames.nextElement();
                String paramValue=httpServletRequest.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body= CodeUtil.decodeURL(StreamUtil.getString(httpServletRequest.getInputStream()));
            if(StringUtils.isNotEmpty(body)){
                String [] params = StringUtils.splitByWholeSeparator(body,"&");
                if (ArrayUtil.isNotEmpty(params)){
                    for (String param : params){
                        String [] array = StringUtils.splitByWholeSeparator(param,"=");
                        if (ArrayUtil.isNotEmpty(array) && array.length == 2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            //通过获取到的参数，创建参数对象
            Param param = new Param(paramMap);
            //调用Action方法
            Method actionMethod = handler.getActionMethod();
            Object result;
            if(param.getMap().size()<=0){
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
            }else {
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            }
            //处理Action方法返回值
            if (result instanceof View){
                //返回jsp页面
                View view = (View) result;
                String path = view.getPath();
                if (StringUtils.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        //重定向
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+path);
                    }else {
                        Map<String,Object> model = view.getModel();
                        for (Map.Entry<String,Object> entry : model.entrySet()){
                            httpServletRequest.setAttribute(entry.getKey(),entry.getValue());
                        }
                        //转发
                        httpServletRequest.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(httpServletRequest,httpServletResponse);
                    }
                }
            }else if (result instanceof Data){
                //返回json数据
                Data data = (Data) result;
                Object model = ((Data) result).getModel();
                if (model != null){
                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    PrintWriter printWriter = httpServletResponse.getWriter();
                    String json = JsonUtil.toJson(model);
                    printWriter.write(json);
                    printWriter.flush();
                    printWriter.close();
                }
            }
        }
    }
}
