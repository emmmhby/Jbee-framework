package group.jbee.framework.helper;

import group.jbee.framework.annotation.Configuration;
import group.jbee.framework.annotation.Controller;
import group.jbee.framework.annotation.Repository;
import group.jbee.framework.annotation.Service;
import group.jbee.framework.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

public final class ClassHelper {
    /**
     * 定义类集合（用于存放所加载的类）
     */
    private static final Set<Class<?>> CLASS_SET;
    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET= ClassUtil.getClassSet(basePackage);
    }
    /**
     * 获取应用包名下的所有类
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }
    /**
     * 获取应用包名下的所有Repository类
     */
    public static Set<Class<?>> getRepositoryClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Repository.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下的所有Service类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下的所有Controller类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下的所有Configuration类
     */
    public static Set<Class<?>> getConfigClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Configuration.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下的所有Bean类
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet =new HashSet<Class<?>>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        beanClassSet.addAll(getConfigClassSet());
        return beanClassSet;
    }
}
