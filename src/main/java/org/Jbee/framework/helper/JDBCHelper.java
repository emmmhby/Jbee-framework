package org.Jbee.framework.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCHelper {
    private static ArrayList<Connection> pool;
    private static String url;
    private static String driver;
    private static String user;
    private static String password;
    private static int min_size;
    private static int max_size;

    public JDBCHelper() {
    }

    private static void init() {
        pool = new ArrayList();

        url = ConfigHelper.getJdbcUrl();
        driver = ConfigHelper.getJdbcDriver();
        user = ConfigHelper.getJdbcUsername();
        password = ConfigHelper.getJdbcPassword();
        min_size = Integer.parseInt(ConfigHelper.getJdbcMinCnn());
        max_size = Integer.parseInt(ConfigHelper.getJdbcMaxCnn());

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
            return;
        }

        initPool();
    }

    private static void initPool() {
        pool.clear();

        for(int i = 0; i < min_size; ++i) {
            try {
                pool.add(DriverManager.getConnection(url,user,password));
            } catch (SQLException var2) {
                var2.printStackTrace();
                return;
            }
        }

    }

    private static Connection getConnection() {
        Connection conn = null;
        if (pool.size() <= 0) {
            try {
                conn = DriverManager.getConnection(url,user,password);
            } catch (SQLException var2) {
                var2.printStackTrace();
                return null;
            }
        } else {
            conn = (Connection)pool.remove(0);

            try {
                if (conn == null || conn.isClosed()) {
                    initPool();
                    conn = (Connection)pool.remove(0);
                }
            } catch (SQLException var3) {
                var3.printStackTrace();
            }
        }

        return conn;
    }

    public static int executeUpdate(String sql, Object... o) {
        Connection conn = getConnection();
        PreparedStatement ps = null;

        try {
            assert conn != null;

            ps = conn.prepareStatement(sql);
        } catch (Exception var9) {
            var9.printStackTrace();
            return freeConnection(conn);
        }

        try {
            ps.executeQuery("show tables");
        } catch (SQLException var8) {
            initPool();
            return executeUpdate(sql, o);
        }

        int i;
        if (o != null) {
            for(i = 0; i < o.length; ++i) {
                try {
                    ps.setObject(i + 1, o[i]);
                } catch (SQLException var7) {
                    var7.printStackTrace();
                    return freeConnection(conn);
                }
            }
        }

        boolean var10 = true;

        try {
            i = ps.executeUpdate();
        } catch (SQLException var6) {
            var6.printStackTrace();
            return freeConnection(conn);
        }

        freeConnection(conn);
        return i;
    }

    public static ResultSet executeQuery(String sql, Object... o) {
        Connection conn = getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
        } catch (Exception var9) {
            var9.printStackTrace();
            freeConnection(conn);
            return null;
        }

        try {
            ps.executeQuery("show tables");
        } catch (SQLException var8) {
            initPool();
            return executeQuery(sql, o);
        }

        if (o != null) {
            for(int i = 0; i < o.length; ++i) {
                try {
                    ps.setObject(i + 1, o[i]);
                } catch (SQLException var7) {
                    var7.printStackTrace();
                    freeConnection(conn);
                    return null;
                }
            }
        }

        ResultSet res = null;

        try {
            res = ps.executeQuery();
        } catch (SQLException var6) {
            var6.printStackTrace();
            return null;
        }

        freeConnection(conn);
        return res;
    }

    private static int freeConnection(Connection conn) {
        if (conn != null) {
            if (pool.size() < max_size) {
                pool.add(conn);
            } else {
                try {
                    conn.close();
                } catch (SQLException var2) {
                    var2.printStackTrace();
                }
            }
        }

        return -1;
    }

    public static void freeResultSet(ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
                return;
            }
        }

    }

    static {
        init();
        initPool();
    }
}
