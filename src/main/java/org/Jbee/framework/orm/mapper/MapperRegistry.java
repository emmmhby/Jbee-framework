package org.Jbee.framework.orm.mapper;

import org.Jbee.framework.orm.session.SqlSession;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    private final Map<Class<?>,MapperProxyFactory<?>>  registeredMapper=new HashMap<Class<?>, MapperProxyFactory<?>>();

    public MapperRegistry() {
    }

    public <T>  boolean hasMapper(Class<T> clazz){
        return registeredMapper.containsKey(clazz);
    }

    public <T> void addMapper(Class<T> tClass) throws Exception {
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

    public <T> T getMapper(Class<T> tClass, SqlSession sqlSession) throws Exception {
        MapperProxyFactory<T> mapperProxyFactory= (MapperProxyFactory<T>) registeredMapper.get(tClass);
        if(mapperProxyFactory==null){
            throw new Exception("Mapper"+tClass+"is not exist");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(registeredMapper.keySet());
    }
}
