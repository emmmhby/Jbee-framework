package group.jbee.framework;

import group.jbee.framework.helper.*;
import group.jbee.framework.util.ClassUtil;

public final class HelperLoad {
    //初始化，加载类
    public static void init(){
        Class<?> [] classList = {ClassHelper.class, BeanHelper.class,ProxyHelper.class, IocHelper.class, ControllerHelper.class,JDBCHelper.class};
        for (Class<?> cls: classList) {
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
