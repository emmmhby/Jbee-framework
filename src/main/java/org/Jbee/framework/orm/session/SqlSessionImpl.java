package org.Jbee.framework.orm.session;

public class SqlSessionImpl implements SqlSession {
    private static SqlSessionImpl sqlSessionImplInstance =new SqlSessionImpl();

    public SqlSessionImpl() {
    }

    public static SqlSessionImpl getSqlsession(){
        if(sqlSessionImplInstance ==null){
            sqlSessionImplInstance =new SqlSessionImpl();
            return sqlSessionImplInstance;
        }
        return sqlSessionImplInstance;
    }
}
