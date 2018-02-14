package group.jbee.framework.orm.builder;

import group.jbee.framework.orm.mapping.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

public class StaticSql {

    private String sql;
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
    private Class returnType;

    public StaticSql(String sql,List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public StaticSql(String sql, List<ParameterMapping> parameterMappings, Class returnType) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.returnType = returnType;
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
