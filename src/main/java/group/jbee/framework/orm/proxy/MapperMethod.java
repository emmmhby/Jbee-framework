package group.jbee.framework.orm.proxy;

import group.jbee.framework.orm.annotation.Delete;
import group.jbee.framework.orm.annotation.Insert;
import group.jbee.framework.orm.annotation.Select;
import group.jbee.framework.orm.annotation.Update;
import group.jbee.framework.orm.builder.SqlBuilder;
import group.jbee.framework.orm.builder.StaticSql;
import group.jbee.framework.orm.mapping.ParameterMapping;
import group.jbee.framework.orm.mapping.SqlCommandType;
import group.jbee.framework.orm.session.SqlSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MapperMethod {

    private final SqlCommand sqlCommand;
    private final MethodSign methodSign;

    public MapperMethod(Class<?> mapperInterface,Method method) {
        this.sqlCommand=new SqlCommand(mapperInterface,method);
        this.methodSign=new MethodSign(mapperInterface,method);
    }
    public Object execute(Method method, Object...args) throws Exception {
        Object result=null;
        switch (sqlCommand.getSqlCommandType()) {
            case INSERT: {
                String originalSql=method.getAnnotation(Insert.class).value();
                StaticSql staticSql=buildStaticSql(originalSql,args);
                if (methodSign.returnVoid) {
                    SqlSession.getSqlsession().insert(staticSql);
                    result = null;
                } else {
                    result = SqlSession.getSqlsession().insert(staticSql);
                }
                break;
            }
            case UPDATE: {
                String originalSql = method.getAnnotation(Update.class).value();
                StaticSql staticSql= buildStaticSql(originalSql,args);
                if (methodSign.returnVoid) {
                    SqlSession.getSqlsession().update(staticSql);
                    result = null;
                } else {
                    result = SqlSession.getSqlsession().update(staticSql);
                }
                break;
            }
            case DELETE: {
                String originalSql= method.getAnnotation(Delete.class).value();
                StaticSql staticSql=buildStaticSql(originalSql,args);
                if (methodSign.returnVoid) {
                    SqlSession.getSqlsession().delete(staticSql);
                    result = null;
                } else {
                    result = SqlSession.getSqlsession().delete(staticSql);
                }
                break;
            }
            case SELECT:{
                String originalSql=method.getAnnotation(Select.class).value();
                StaticSql staticSql=buildStaticSql(originalSql,args);
                if (methodSign.returnVoid) {
                    result = null;
                } else if (methodSign.returnMany) {
                    result = SqlSession.getSqlsession().selectList(staticSql);
                } else {
                    result = SqlSession.getSqlsession().selectOne(staticSql);
                }
                break;
             }
            default:{
                throw new Exception("Unknown execution method for: " + sqlCommand.getName());
            }

        }
        return result;
}
    private StaticSql buildStaticSql(String originalSql, Object...args){
        Class<?> clazz = args[0].getClass();
        Field[] files=clazz.getDeclaredFields();
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        if(files != null){
            for (int i = 0; i < args.length; i++) {
                for (int j = 0; j < files.length; j++){
                    try {
                        files[j].setAccessible(true);
                        ParameterMapping.Builder builder = new ParameterMapping.Builder(files[j].getName(),files[j].get(args[i]));
                        parameterMappings.add(builder.build());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        return sqlBuilder.parse(originalSql,parameterMappings,methodSign.getReturnType());

    }
    public static class  MethodSign{
        private final boolean returnMany;
        private final boolean returnVoid;
        private final Class returnType;
        private final Type genericReturnType;

        public MethodSign(Class<?> mapperInterface,Method method){
            this.returnType = method.getReturnType();
            this.returnVoid = void.class.equals(this.returnType);
            this.returnMany = (Collection.class.isAssignableFrom(this.returnType) || this.returnType.isArray());
            this.genericReturnType=method.getGenericReturnType();
        }

        public boolean isReturnMany() {
            return returnMany;
        }

        public boolean isReturnVoid() {
            return returnVoid;
        }

        public Class<?> getReturnType() {
            if(returnMany){
                ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                Type[] actualTypeArguments =parameterizedType.getActualTypeArguments();
                return (Class<?>) actualTypeArguments[0];
            }
            return returnType;
        }
    }
    public static class SqlCommand{
        private final String name;
        private final SqlCommandType sqlCommandType;
        private final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<Class<? extends Annotation>>();

        public SqlCommand(Class<?>mapperInterface, Method method){
            final String methodName=method.getName();
            sqlAnnotationTypes.add(Select.class);
            sqlAnnotationTypes.add(Insert.class);
            sqlAnnotationTypes.add(Update.class);
            sqlAnnotationTypes.add(Delete.class);
            name=mapperInterface.getName()+"."+methodName;
            sqlCommandType=getSqlCommandType(method);
        }
        private SqlCommandType getSqlCommandType(){
            return sqlCommandType;
        }

        public String getName() {
            return name;
        }

        private SqlCommandType getSqlCommandType(Method method){
            Class<? extends Annotation> type = getSqlAnnotationType(method,sqlAnnotationTypes);
            if (type == null) {
                return SqlCommandType.UNKNOWN;
            }
            return SqlCommandType.valueOf(type.getSimpleName().toUpperCase(Locale.ENGLISH));
        }

        private Class<? extends Annotation> getSqlAnnotationType(Method method, Set<Class<? extends Annotation>> types){
            for (Class<? extends Annotation> type : types) {
                Annotation annotation = method.getAnnotation(type);
                if (annotation != null) {
                    return type;
                }
            }
            return null;
        }
    }
}
