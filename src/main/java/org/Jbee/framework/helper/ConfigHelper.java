package org.Jbee.framework.helper;

import org.Jbee.framework.ConfigConstant;
import org.Jbee.framework.util.PropsUtil;

import java.util.Properties;

/**
 * 属性文件助手类
 * @author huangbaoyuan
 * @version 1.0.0
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROS= PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取JDBC驱动
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_DRIVER);
    }
    /**
     * 获取JDBC URL
     */
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_URL);
    }
    /**
     * 获取JDBC 用户名
     */
    public static String  getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_USERNAME);
    }
    /**
     * 获取JDBC 密码
     */
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_PASSWORD);
    }
    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.APP_BASE_PACKAGE);
    }
    /**
     * 获取应用JSP路径
     */
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.APP_JSP_PATH,"/view/");
    }
    /**
     * 获取静态资源路径
     */
    public static String getAppStaticPath(){
        return PropsUtil.getString(CONFIG_PROS,ConfigConstant.APP_STATIC_PATH,"/static/");
    }
}
