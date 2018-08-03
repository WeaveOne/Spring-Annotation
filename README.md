# Spring注解开发-全面解析常用注解使用方法

---

[TOC]



## 1. @Configuration

​	`@Configuration` //配置注解类似 applicationcontext.xml 只是将xml配置改为 注解方式进行

## 2. @ComponentScan

进行包扫描会根据注解进行注册组件,value="包名"

```java
@ComponentScan(value="cn.willvi")
```

 ### FilterType  

- ANNOTATION 通过注解类型 列如 @Controller为Controller.class @Service 为 Service.class
- ASSIGNABLE_TYPE, 一组具体类 例如PersonController.class
- ASPECTJ, 一组表达式,使用Aspectj表达式命中类
- REGEX 一组表达式,使用正则命中类
- CUSTOM 自定义的TypeFilter.

###  excludeFilters 

​	 excludeFIlters = Filter[] 根据规则排除组件

```java
@ComponentScan(value="cn.willvi",excludeFilters= {
  	    //根据注解排除注解类型为@Controller
		@Filter(type=FilterType.ANNOTATION,value= {Controller.class}),
		@Filter(type=FilterType.ASSIGNABLE_TYPE,value= {IncludeConfig.class,MainConfig.class}),
})
```

### includeFIlters

​	includeFIlters = Filter[]  根据规则只包含哪些组件（**ps：useDefaultFilters设置为false**）

```java
@ComponentScan(value="cn.willvi",includeFilters= {
        //根据注解类型扫描注解类型为@Controller的类
		@Filter(type=FilterType.ANNOTATION,value= {Controller.class})
},useDefaultFilters=false)
```

### 使用自定义TypeFilter

​	当过滤有特殊要求时，可以实现TypeFilter来进行自定的过滤规则

自定义TypeFilter:

```java
public class CustomTypeFilter implements TypeFilter {
	/**
	 * metadataReader the metadata reader for the target class 读取当前扫描类的信息
	 * metadataReaderFactory a factory for obtaining metadata readers
	 * for other classes (such as superclasses and interfaces) 获取其他类的信息
	 */
	public boolean match(MetadataReader reader, MetadataReaderFactory factory) throws IOException {
		//获取当前扫描类信息
		ClassMetadata classMetadata = reader.getClassMetadata();
		//获取当前注解信息
		AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
		//获取当前类资源(类路径)
		Resource resource = reader.getResource();
		String className = classMetadata.getClassName();
		System.out.println("----->"+className);
		if(className.contains("PersonService")) {
			return true;
		}
		return false;
	}
}
```

使用：

```java
//自定义过滤组件
@ComponentScan(value="cn.willvi",includeFilters= {
		@Filter(type=FilterType.CUSTOM,value= {CustomTypeFilter.class})
},useDefaultFilters=false)
//或者
//自定义过滤组件
@ComponentScan(value="cn.willvi",excludeFilters= {
		@Filter(type=FilterType.CUSTOM,value= {CustomTypeFilter.class})})
```

## 3. @Bean

​	注册bean与spring 的xml配置异曲同工之妙只是将xml配置转换为注解

```xml
  <bean id="person" class="cn.willvi.bean.Person"  scope="prototype" >
		<property name="age" value="23"></property>
		<property name="name" value="willvi"></property>
	</bean>
```

### @Scope

​	在 Spring IoC 容器是指其创建的 Bean 对象相对于其他 Bean 对象的请求可见范围。

 -  singleton单例模式  全局有且仅有一个实例

- prototype原型模式 每次获取Bean的时候会有一个新的实例

- request 每一次HTTP请求都会产生一个新的bean，同时该bean仅在当前HTTP request内有效

- session  session作用域表示该针对每一次HTTP请求都会产生一个新的bean，同时该bean仅在当前HTTP session内有效

- global session global session作用域类似于标准的HTTP Session作用域，不过它仅仅在基于portlet的web应用中才有意义。

  **以上5个一般只用第一个和第二个**

原型模式使用：

```java
	@Bean
	@Scope("prototype")
	public Person person() {
		return new Person("willvi",23);
	}
```

验证：

```java
	    ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		Person person = (Person) ioc.getBean("personScope");
		Person person1 = (Person) ioc.getBean("personScope");
		//返回true说明为单例
		System.out.println(person==person1);
```

### @Lazy

​	懒加载。当Scope为单例模式时，当容器被初始化时就会被实例化。

​	当有@Lazy时，在容器初始化时不会被实例化，在获取实例时才会被初始化

单例模式懒加载使用

```JAVA
@Bean
	@Scope
	@Lazy //去掉和加上看输出结果
	public Person person() {
        System.out.println("bean初始化");
		return new Person("willvi",23);
	}
```

验证：

```java
    ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
	System.out.println("容器初始化完成");
	Person person = (Person) ioc.getBean("personScope");
```

