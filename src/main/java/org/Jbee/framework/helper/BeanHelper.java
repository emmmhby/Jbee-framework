package org.Jbee.framework.helper;

import org.Jbee.framework.util.ReflectionUtil;

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
