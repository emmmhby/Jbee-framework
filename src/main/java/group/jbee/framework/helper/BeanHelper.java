package group.jbee.framework.helper;

import group.jbee.framework.annotation.Configuration;
import group.jbee.framework.annotation.Bean;
import group.jbee.framework.util.ReflectionUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {
    /**
     * 定义bean映射
     */
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>, Object>();
    static {
        Set<Class<?>> beanClassSet=ClassHelper.getBeanClassSet();
        for(Class<?> beanClass:beanClassSet){
            Object obj= ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass,obj);
            if(beanClass.isAnnotationPresent(Configuration.class)){
                Method[] methods=beanClass.getDeclaredMethods();
                for (Method method:methods){
                    if(method.isAnnotationPresent(Bean.class)){
                        Class methodReturnType=method.getReturnType();
                        if(methodReturnType==null){
                            throw new NullPointerException("Method can't return void");
                        }
                        if(BEAN_MAP.get(methodReturnType)==null){
                            synchronized (BEAN_MAP){
                                try {
                                    Object object= method.invoke(obj);
                                    BEAN_MAP.put(methodReturnType,object);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 获取bean映射
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }
    /**
     * 获取bean实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> tClass){
        if(!BEAN_MAP.containsKey(tClass)){
            throw new RuntimeException("cant get bean by class :"+tClass);
        }
        return (T) BEAN_MAP.get(tClass);
    }
}
