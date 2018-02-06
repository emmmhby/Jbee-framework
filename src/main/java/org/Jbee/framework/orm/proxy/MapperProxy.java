package org.Jbee.framework.orm.proxy;

import org.Jbee.framework.orm.annotation.Select;
import org.Jbee.framework.orm.builder.SqlBuilder;
import org.Jbee.framework.orm.builder.StaticSql;
import org.Jbee.framework.orm.mapping.ParameterMapping;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MapperProxy<T> implements InvocationHandler{

    private final Class<T> mapperInterface;

    public MapperProxy(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("The method is:"+method.getName());
        System.out.println(method.getAnnotation(Select.class).value());
        String originalSql=method.getAnnotation(Select.class).value();
        Class<?> clazz = args[0].getClass();
        Field[] files=clazz.getDeclaredFields();
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        if(files != null){
            for (int i = 0; i < args.length; i++) {
                for (int j = 0; j < files.length; j++){
                    try {
                        files[j].setAccessible(true);
                        ParameterMapping.Builder builder = new ParameterMapping.Builder(files[j].getName(),files[j].get(args[i]));
                        parameterMappings.add(builder.build());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        StaticSql staticSql= sqlBuilder.parse(originalSql,parameterMappings);
        return staticSql;
    }
}
