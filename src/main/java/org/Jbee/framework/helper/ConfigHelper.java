package org.Jbee.framework.helper;

import org.Jbee.framework.ConfigConstant;
import org.Jbee.framework.util.PropertiesUtil;

import java.util.Properties;

/**
 * 属性文件助手类
 * @author huangbaoyuan
 * @version 1.0.0
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROS= PropertiesUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取JDBC驱动
     */
    public static String getJdbcDriver(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_DRIVER);
    }
    /**
     * 获取JDBC URL
     */
    public static String getJdbcUrl(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_URL);
    }
    /**
     * 获取JDBC 用户名
     */
    public static String  getJdbcUsername(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_USERNAME);
    }
    /**
     * 获取JDBC 密码
     */
    public static String getJdbcPassword(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_PASSWORD);
    }
    /**
     * 获取JDBC 连接池最大数量
     */
    public static String getJdbcMaxCnn(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_MAXCNN);
    }
    /**
     * 获取JDBC 连接池最小数量
     */
    public static String getJdbcMinCnn(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.JDBC_MINCNN);
    }
    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.APP_BASE_PACKAGE);
    }
    /**
     * 获取应用JSP路径
     */
    public static String getAppJspPath(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.APP_JSP_PATH,"/view/");
    }
    /**
     * 获取静态资源路径
     */
    public static String getAppStaticPath(){
        return PropertiesUtil.getString(CONFIG_PROS,ConfigConstant.APP_STATIC_PATH,"/static/");
    }
}
