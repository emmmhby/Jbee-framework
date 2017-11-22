package org.Jbee.framework.helper;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.Jbee.framework.orm.mapper.MapperProxy;
import org.Jbee.framework.orm.mapper.MapperRegistry;
import org.Jbee.framework.orm.session.SqlSession;
import org.Jbee.framework.util.ClassUtil;
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
