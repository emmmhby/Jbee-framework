package org.Jbee.framework.orm.mapping;

public class ParameterMapping {
    private String expression;
    private Object parameter;

    public ParameterMapping() {
    }
    public static class Builder{
        private ParameterMapping parameterMapping=new ParameterMapping();

        public Builder(String expression,Object parameter ) {
            parameterMapping.expression = expression;
            parameterMapping.parameter  = parameter;
        }

        public Builder expression(String expression) {
            parameterMapping.expression = expression;
            return this;
        }

        public Builder parameter(Object parameter) {
            parameterMapping.parameter = parameter;
            return this;
        }

        public ParameterMapping build(){
            return parameterMapping;
        }
    }

    public String getExpression() {
        return expression;
    }

    public Object getParameter() {
        return parameter;
    }
}
