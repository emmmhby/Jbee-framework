package org.Jbee.framework.orm.proxy;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MapperRegistry {

    private static final Map<Class<?>,MapperProxyFactory<?>>  registeredMapper=new HashMap<Class<?>, MapperProxyFactory<?>>();

    public MapperRegistry() {
    }

    public static <T>  boolean hasMapper(Class<T> clazz){
        return registeredMapper.containsKey(clazz);
    }

    public static <T> void addMapper(Class<T> tClass) throws Exception {
        if(tClass.isInterface()){
            if(hasMapper(tClass)){
                throw new Exception("Mapper"+tClass+"is already Registry");
            }
            boolean hasRegistry=false;
            try{
                registeredMapper.put(tClass,new MapperProxyFactory<T>(tClass));//注册Mapper接口
                hasRegistry=true;
            }finally {
                if(!hasRegistry){
                    registeredMapper.remove(tClass);
                }
            }
        }
    }

    public static  <T> T getMapper(Field tClass) throws Exception {
        MapperProxyFactory<T> mapperProxyFactory= (MapperProxyFactory<T>) registeredMapper.get(tClass.getType());
        if(mapperProxyFactory==null){
            throw new Exception("Mapper"+tClass+"is not exist");
        }
        try {
            return mapperProxyFactory.newInstance();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(registeredMapper.keySet());
    }
}
