package org.Jbee.framework.orm.parser;

import org.Jbee.framework.orm.mapping.ParameterMapping;

import java.util.List;
import java.util.Map;

public interface TokenHandler {
    String handleToken(String content);
    Map<String, Object> getParameterMap();
    List<ParameterMapping> getParameterMappings();
}
