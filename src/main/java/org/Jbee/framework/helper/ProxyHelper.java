package org.Jbee.framework.helper;

import org.Jbee.framework.orm.proxy.MapperRegistry;
import org.Jbee.framework.util.CollectionUtil;

import java.util.Set;

public class ProxyHelper {
    static {
        Set<Class<?>> mapperClassSet =  ClassHelper.getRepositoryClassSet();
        if(CollectionUtil.isNotEmpty(mapperClassSet)){
            for (Class<?> mapperClass :
                    mapperClassSet) {
                try {
                    MapperRegistry.addMapper(mapperClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
