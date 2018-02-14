package group.jbee.framework.helper;

import group.jbee.framework.orm.proxy.MapperRegistry;
import group.jbee.framework.util.CollectionUtil;

import java.util.Set;

public class ProxyHelper {
    static {
        Set<Class<?>> mapperClassSet =  ClassHelper.getRepositoryClassSet();
        if(CollectionUtil.isNotEmpty(mapperClassSet)){
            for (Class<?> mapperClass : mapperClassSet) {
                try {
                    MapperRegistry.addMapper(mapperClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
