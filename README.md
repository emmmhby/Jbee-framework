## Jbee -  轻量级MVC框架

##### 快速了解请配合[Demo](https://github.com/emmmhby/Jbeedemo)阅读本文档

### 快速使用

#### Maven
```XML
<dependency>
    <groupId>group.jbee</groupId>
    <artifactId>jbee-framework</artifactId>
    <version>1.0.1</version>
</dependency>
```
还未上传Maven仓库，找不到依赖可clone本项目，执行==mvn
install==打包到本地仓库。也可以直接下载jar包，[点此下载](https://pan.baidu.com/s/1mkmpWWK)。

#### 其他依赖
MySQL驱动
```XML
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.33</version>
</dependency>
```
#### 配置文件
在==resource==目录下新建==jbee.properties==配置文件，配置项如下

```Java
/***JDBC 驱动*/
jbee.framework.jdbc.driver=com.mysql.jdbc.Driver
/***JDBC URL*/
jbee.framework.jdbc.url=jdbc:mysql://localhost:3306/jbee
/***JDBC 用户名*/
jbee.framework.jdbc.username=root
/***JDBC 密码*/
jbee.framework.jdbc.password=123456
/***JDBC 连接池最大数量*/
jbee.framework.jdbc.max=5
/***JDBC 连接池最小数量*/
jbee.framework.jdbc.min=2
/***应用基础包名*/
jbee.framework.app.base_package=group.jbee.jbeedemo
/***JSP路径*/
jbee.framework.app.jsp_path=/view/
/***静态资源路径*/
jbee.framework.app.static_path=/static/
```
当然，还有web.xml配置

```XML
<servlet-mapping>
    <servlet-name>defaultservlet</servlet-name>
    <url-pattern>/*</url-pattern>
</servlet-mapping>
<servlet>
    <servlet-name>defaultservlet</servlet-name>
    <servlet-class>group.jbee.framework.DispatcherServlet</servlet-class>
    <load-on-startup>0</load-on-startup>
</servlet>
```
以上就是所有必要的配置，下面看如何使用

### Controller
与Spring MVC差不多，使用 ==@Controller==注解来修饰控制器类，使用 ==@Action==注解来匹配URL

示例代码：
```Java
@Controller
public class HelloAction {

    @Inject
    private UserService userService;

    //返回json数据
    @Action("POST:/Action1")
    public Data action1(Param param){
        Map<String,Object> params = param.getMap();
        String name = (String) params.get("name");
        User user= new User();
        user.setName(name);
        Data data=new Data(userService.getUserInfo(user));
        return data;
    }
    //返回jsp页面
    @Action("GET:/Action2")
    public View action1(){
        View view = new View("hello.jsp");
        //使用EL表达式${data}取值
        view.setModel("data","HelloJbee!";
        return view;
    }
}
```
### DAO
持久层与Mybatis类似，只需编写DAO接口以及SQL语句，目前只支持静态SQL。接口用注解 ==@Repository==声明，方法用 ==@Select==、==@Insert==、==@Delete==、==@Update==来声明。
 
 示例代码：
 
```Java
@Repository
public interface UserMapper {

    @Select("select * from users where name=#{name}")
    List<User> getUserInfo(User user);

    @Insert(value = "insert into users(name,password) values(#{name},#{password})")
    void createUser(User user);
}
```
### IOC
IOC是Spring的核心，Jbee也实现了一个简单的IOC容器。
使用注解 ==@Inject==修饰需要注入的类。

示例代码：


```Java
@Controller
public class HelloAction {

    @Inject
    private UserService userService;

    ......
}
```
```Java
@Service
public class UserService {
    
    @Inject
    UserMapper userMapper;

    public List<User> getUserInfo(User user){
        return userMapper.getUserInfo(user);
    }
    public void createUser(User user){
        userMapper.createUser(user);
    }
}
```
