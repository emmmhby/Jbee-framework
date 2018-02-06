package org.Jbee.framework.orm.builder;

import org.Jbee.framework.orm.mapping.ParameterMapping;
import org.Jbee.framework.orm.parser.TokenHandler;
import org.Jbee.framework.orm.parser.TokenParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlBuilder {

    public StaticSql parse(String originalSql,List<ParameterMapping> parameterMappings) {
        TokenHandler handler = new ParamTokenHandler(parameterMappings);
        TokenParser parser = new TokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSql(sql, handler.getParameterMap(),handler.getParameterMappings());
    }

    private static class ParamTokenHandler implements TokenHandler {
        private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        private Map<String,Object> parameterMap = new HashMap<String, Object>();

        public ParamTokenHandler(List<ParameterMapping> parameterMappings) {
            this.parameterMappings = parameterMappings;
            for (ParameterMapping parameterMapping : parameterMappings){
                parameterMap.put(parameterMapping.getExpression(),parameterMapping.getParameter());
            }
            parameterMappings.clear();
        }

        @Override
        public Map<String, Object> getParameterMap() {
            return parameterMap;
        }

        @Override
        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(content,parameterMap.get(content));
            parameterMappings.add(builder.build());
            return "?";
        }
    }
}
