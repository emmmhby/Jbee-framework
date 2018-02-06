package org.Jbee.framework.helper;

import org.Jbee.framework.annotation.Inject;
import org.Jbee.framework.annotation.Service;
import org.Jbee.framework.orm.proxy.MapperRegistry;
import org.Jbee.framework.orm.session.SqlSessionImpl;
import org.Jbee.framework.util.ArrayUtil;
import org.Jbee.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public final class IocHelper {
    static SqlSessionImpl sqlSessionImpl;
    static {
        try{
            //获取所有Bean类与Bean实例的映射关系
            Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
            //遍历Bean Map
            for (Map.Entry<Class<?>,Object> beanEntry:beanMap.entrySet()){
                //从BeanMap中获取bean类与bean实例
                Class<?> beanClass=beanEntry.getKey();
                Object beanInstance=beanEntry.getValue();
                //获取bean类定义的所有成员变量
                Field[] beanFields=beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    //遍历bean Field
                    for(Field beanField:beanFields){
                        //判断当前bean Field是否带有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            if(beanClass.isAnnotationPresent(Service.class)){
                                Object beanFieldInstance = MapperRegistry.getMapper(beanField);
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }else {
                                // 在Bean Map中获取Bean Field对应的实例
                                Class<?> beanFieldClass = beanField.getType();
                                Object beanFieldInstance = beanMap.get(beanFieldClass);
                                if (beanFieldInstance != null) {
                                    //通过反射初始号BeanField的值
                                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("初始化 IocHelper 出错！", e);
        }
    }

}
