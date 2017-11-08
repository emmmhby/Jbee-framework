package org.Jbee.framework.helper;

import org.Jbee.framework.bean.Request;
import org.Jbee.framework.util.ArrayUtil;
import org.Jbee.framework.util.CollectionUtil;
import org.Jbee.framework.annotation.Action;
import org.Jbee.framework.bean.Handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ControllerHelper {
    private static final Map<Request,Handler> ACTION_MAP=new HashMap<Request, Handler>();
    static {
        Set<Class<?>> controllerClassSet=ClassHelper.getControllerClassSet();
        if(CollectionUtil.isNotEmpty(controllerClassSet)){
            for (Class<?> controllerClass:controllerClassSet){
                Method[] methods=controllerClass.getDeclaredMethods();
                for (Method method:methods){
                    if(method.isAnnotationPresent(Action.class)){
                        Action action=method.getAnnotation(Action.class);
                        String mapping = action.value();
                        if(mapping.matches("\\w+:/\\w*")){
                            String[] array = mapping.split(":");
                            if(ArrayUtil.isNotEmpty(array)&&array.length==2){
                                //获取请求方法与路径
                                String requestMethod=array[0];
                                String requestPath = array[1];
                                Request request=new Request(requestMethod,requestPath);
                                Handler handler=new Handler(controllerClass,method);
                                ACTION_MAP.put(request,handler);
                            }
                        }
                    }
                }
            }
        }
    }
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request=new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
