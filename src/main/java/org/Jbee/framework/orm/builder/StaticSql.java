package org.Jbee.framework.orm.builder;

import org.Jbee.framework.orm.mapping.ParameterMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticSql {

    private String sql;
    private Map<String,Object> parameterMap = new HashMap<String, Object>();
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    public StaticSql(String sql, Map<String, Object> parameterMap,List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMap = parameterMap;
        this.parameterMappings = parameterMappings;
    }
}
