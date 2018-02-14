package group.jbee.framework.orm.session;

import group.jbee.framework.helper.JDBCHelper;
import group.jbee.framework.orm.builder.StaticSql;
import group.jbee.framework.orm.mapping.ParameterMapping;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlSession {
    private static SqlSession sqlSessionInstance =new SqlSession();

    public SqlSession() {
    }


    public static SqlSession getSqlsession(){
        if(sqlSessionInstance ==null){
            sqlSessionInstance =new SqlSession();
            return sqlSessionInstance;
        }
        return sqlSessionInstance;
    }
    private List doQuery(ResultSet rs, Class clazz) {
        ArrayList list = new ArrayList();
        Field[] fields = clazz.getDeclaredFields();

        try {
            while(rs.next()) {
                Object e = clazz.newInstance();

                for(int i = 0; i < fields.length; ++i) {
                    fields[i].setAccessible(true);
                    fields[i].set(e, rs.getObject(fields[i].getName()));
                }

                list.add(e);
            }

            JDBCHelper.freeResultSet(rs);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return list;
    }

    public List selectList(StaticSql staticSql) {
        List<ParameterMapping> list=staticSql.getParameterMappings();
        List<Object> parameters=new ArrayList<Object>();
        for (ParameterMapping mapping:list){
            parameters.add(mapping.getParameter());
        }
        ResultSet resultSet= JDBCHelper.executeQuery(staticSql.getSql(),parameters);
        List resultList=this.doQuery(resultSet,staticSql.getReturnType());
        return resultList;
    }
    public Object selectOne(StaticSql staticSql){
        return selectList(staticSql).get(0);
    }
    public int update(StaticSql staticSql){
        List<ParameterMapping> list=staticSql.getParameterMappings();
        List<Object> parameters=new ArrayList<Object>();
        for (ParameterMapping mapping:list){
            parameters.add(mapping.getParameter());
        }
        int result=JDBCHelper.executeUpdate(staticSql.getSql(),parameters);
        return result;
    }
    public int insert(StaticSql staticSql){
        return update(staticSql);
    }
    public int delete(StaticSql staticSql){
        return update(staticSql);
    }
}
