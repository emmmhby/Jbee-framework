package org.Jbee.framework.orm.proxy;

import org.Jbee.framework.orm.annotation.Delete;
import org.Jbee.framework.orm.annotation.Insert;
import org.Jbee.framework.orm.annotation.Select;
import org.Jbee.framework.orm.annotation.Update;
import org.Jbee.framework.orm.mapping.SqlCommandType;
import org.Jbee.framework.orm.session.SqlSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MapperMethod {

    private final SqlCommand sqlCommand;
    private final MethodSign methodSign;

    public MapperMethod(Class<?> mapperInterface,Method method) {
        this.sqlCommand=new SqlCommand(mapperInterface,method);
        this.methodSign=new MethodSign(mapperInterface,method);
    }
    public Object execute(SqlSession sqlSession, Object...args) throws Exception {
        Object result=null;
        switch (sqlCommand.getSqlCommandType()){
            case INSERT:{
                break;
            }
            case UPDATE:{

                break;
            }
            case DELETE:{
                break;
            }
            case SELECT:

                break;
            default:
                throw new Exception("Unknown execution method for: " + sqlCommand.getName());
        }

        return result;
    }

    public static class  MethodSign{
        private final boolean returnMany;
        private final boolean returnVoid;
        private final Class<?> returnType;

        public MethodSign(Class<?> mapperInterface,Method method){
            this.returnType = method.getReturnType();
            this.returnVoid = void.class.equals(this.returnType);
            this.returnMany = (Collection.class.isAssignableFrom(this.returnType) || this.returnType.isArray());
        }

        public boolean isReturnMany() {
            return returnMany;
        }

        public boolean isReturnVoid() {
            return returnVoid;
        }

        public Class<?> getReturnType() {
            return returnType;
        }
    }
    public static class SqlCommand{
        private final String name;
        private final SqlCommandType sqlCommandType;
        private final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<Class<? extends Annotation>>();

        public SqlCommand(Class<?>mapperInterface, Method method){
            final String methodName=method.getName();
            final Class<?> declaringClass=method.getDeclaringClass();
            sqlAnnotationTypes.add(Select.class);
            sqlAnnotationTypes.add(Insert.class);
            sqlAnnotationTypes.add(Update.class);
            sqlAnnotationTypes.add(Delete.class);
            name=declaringClass.getName()+"."+methodName;
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
