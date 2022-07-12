# Functions List

## Show Prometheus Endpoint
1. pom.xml 
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <scope>runtime</scope>
</dependency>
```
2. configure applicaiton.yaml, include prometheus 
3. show metrics formated for prometheus
http://127.0.0.1:8080/actuator/prometheus


## add prometheus


```
docker run \
    --name prometheus \
    --network host \
    -v /vagrant/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
    -d \
    prom/prometheus
```

visit: 
http://192.168.33.10:9090/targets  
http://192.168.33.10:9090/


## add Grafana
```
docker run --name grafana -d -p 3000:3000 grafana/grafana
```
http://192.168.33.10:3000/



## customization endpoint
1. create endpoint
com.example.learn.observability.endpoint.LogStatsEndpoint  
2. Use @Bean injected into Spring IOC
```
@Bean
public LogStatsEndpoint orderStatsEndpoint(){
    return new LogStatsEndpoint();
}
```
3. exposure endpoint
```
management:
  endpoints:
    web:
      exposure:
        include: logStats
```
4. visit the endpoint
- @ReadOperation
http://127.0.0.1:8080/actuator/logStats  
http://127.0.0.1:8080/actuator/logStats/error  
- @WriteOperation
```
curl -X POST -d '{"logLevel":"error","records":"0"}' --header "Content-Type: application/json" http://127.0.0.1:8080/actuator/logStats
```

参考：  
https://blog.csdn.net/LightOfMiracle/article/details/80594795  

## Get git commit information
Use info endpoint to expose git properties by git.properties.
1. how to generate git information(git.properties)  
add plugin "git-commit-id-plugin" in pom.xml
```
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
</plugin>
```
2. expose the full content of git.properties
the info endpoint exposes git.branch,git.commit.id,git.commit.time properties by default. 
eg:   
http://192.168.33.1:8081/actuator/info  
{"git":{"branch":"master","commit":{"id":"96b8929","time":"2022-07-08T02:33:49Z"}}}

config application.yaml to expose the full content:
```
management.info.git.mode=full
```
 

3. generate git.properties
mvn clean compile , generate META-INF/build-info.properties

Reference:  
https://docs.spring.io/spring-boot/docs/2.7.1/reference/html/actuator.html#actuator.endpoints.info.git-commit-information

## Get build infromation
- add plugin to pom.xml： 
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
- mvn clean compile 

## Exposes any property from the Environment whose name starts with info.

# Reading Source Code 
## default expose endpoints of different types
org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration.webExposeExcludePropertyEndpointFilter  
org.springframework.boot.actuate.autoconfigure.endpoint.expose.EndpointExposure  

## 处理@Endpoint流程
总入口：org.springframework.boot.actuate.autoconfigure.endpoint.web.servlet.WebMvcEndpointManagementContextConfiguration.webEndpointServletHandlerMapping  

1. 查找并生成ExposableWebEndpoint列表
org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration.webEndpointDiscoverer
org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer.getEndpoints

2. 生成并注册RequestMappingInfo
- org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping.initHandlerMethods  
- org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping.registerMapping  
- org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping.createRequestMappingInfo  

流程总结：  
- 扫描所有@Endpoint注解的类，这些类都是Endpoint；
- 使用过滤器对Endpoint对象进行过滤，没有被过滤掉的才可以进入下一步；
- 读取Endpoint对象的每个方法，判断是否有@ReadOperation、@WriteOperation、@DeleteOperation三个注解，如果有，则针对每个被注解的方法创建操作对象Operation；
- 根据操作对象、Endpoint对象、Endpoint名创建为RequestMappingInfo，并将其注册到spring mvc中；
- 注册成功之后，Endpoint对象便可以对外提供服务。 

## Spring容器中扫描HealthContributor实例并加入DefaultHealthContributorRegistry流程
- Declare @Bean of HealthContributorRegistry , get all HealthContributor instance in IOC  assign to variable healthContributors
```
	@Bean
	@ConditionalOnMissingBean
	HealthContributorRegistry healthContributorRegistry(ApplicationContext applicationContext,
			HealthEndpointGroups groups) {
		Map<String, HealthContributor> healthContributors = new LinkedHashMap<>(
				applicationContext.getBeansOfType(HealthContributor.class));
		if (ClassUtils.isPresent("reactor.core.publisher.Flux", applicationContext.getClassLoader())) {
			healthContributors.putAll(new AdaptedReactiveHealthContributors(applicationContext).get());
		}
		return new AutoConfiguredHealthContributorRegistry(healthContributors, groups.getNames());
	}
```
- initialize AutoConfiguredHealthContributorRegistry object with healthContributors 
```
	DefaultContributorRegistry(Map<String, C> contributors, Function<String, String> nameFactory) {
		Assert.notNull(contributors, "Contributors must not be null");
		Assert.notNull(nameFactory, "NameFactory must not be null");
		this.nameFactory = nameFactory;
		Map<String, C> namedContributors = new LinkedHashMap<>();
		contributors.forEach((name, contributor) -> namedContributors.put(nameFactory.apply(name), contributor));
		this.contributors = Collections.unmodifiableMap(namedContributors);
	}
```

org.springframework.boot.actuate.autoconfigure.health.HealthEndpointConfiguration.healthContributorRegistry
org.springframework.boot.actuate.health.DefaultContributorRegistry.DefaultContributorRegistry(java.util.Map<java.lang.String,C>, java.util.function.Function<java.lang.String,java.lang.String>)

## Dynamic configure logger with /loggers endpoint
- exposure loggers endpoint
```
management.endpoints.web.exposure.include=loggers
```
- get the all loggers 
http://127.0.0.1:8081/actuator/loggers

- configure logger level 
```
 curl -H "Content-Type:application/json" -X POST -d '{"configuredLevel": "DEBUG"}' http://127.0.0.1:8081/actuator/loggers/com.example.learn.observability.LogLevelConfigController
```

- restore logger level
```
curl -H "Content-Type:application/json" -X POST -d '{"configuredLevel": null}' http://127.0.0.1:8081/actuator/loggers/com.example.learn.observability.LogLevelConfigController
```

- test logger api
http://127.0.0.1:8080/log


## Registering Custom Metrics
com.example.learn.observability.QueueBeanPostProcessor

Reference:  
https://docs.spring.io/spring-boot/docs/2.7.1/reference/html/actuator.html#actuator.metrics.registering-custom

# Learned Knowledge
# Get properties with Environment
org.springframework.boot.actuate.autoconfigure.endpoint.EndpointIdTimeToLivePropertyFunction.apply
```
@Override
public Long apply(EndpointId endpointId) {
    String name = String.format("management.endpoint.%s.cache.time-to-live", endpointId.toLowerCaseString());
    BindResult<Duration> duration = Binder.get(this.environment).bind(name, DURATION);
    return duration.map(Duration::toMillis).orElse(null);
}
```
# 实现map的并发安全修改
org.springframework.boot.actuate.health.DefaultContributorRegistry.registerContributor
```
private final Object monitor = new Object();
@Override
public void registerContributor(String name, C contributor) {
    synchronized (this.monitor) {
        Assert.state(!this.contributors.containsKey(adaptedName),
                () -> "A contributor named \"" + adaptedName + "\" has already been registered");
        Map<String, C> contributors = new LinkedHashMap<>(this.contributors);
        contributors.put(adaptedName, contributor);
        this.contributors = Collections.unmodifiableMap(contributors);
    }
}
```
# ConditionalOnEnabledHealthIndicator 实现原理
org.springframework.boot.actuate.autoconfigure.health.OnEnabledHealthIndicatorCondition  
org.springframework.boot.actuate.autoconfigure.OnEndpointElementCondition.getMatchOutcome  

org.springframework.boot.actuate.autoconfigure.OnEndpointElementCondition.getEndpointOutcome：  
```
protected ConditionOutcome getEndpointOutcome(ConditionContext context, String endpointName) {
    Environment environment = context.getEnvironment();
    String enabledProperty = this.prefix + endpointName + ".enabled";
    if (environment.containsProperty(enabledProperty)) {
        // 默认为true，所有无需为每个health的endpoint配置：management.health.{具体health名称}.enabled=true
        boolean match = environment.getProperty(enabledProperty, Boolean.class, true);
        return new ConditionOutcome(match, ConditionMessage.forCondition(this.annotationType)
                .because(this.prefix + endpointName + ".enabled is " + match));
    }
    return null;
}

```
# Load bean from spring.factories with SpringFactoriesLoader.loadFactories

sources:  
org.springframework.boot.context.logging.LoggingApplicationListener.onApplicationEvent  
org.springframework.boot.logging.LoggingSystem.get(java.lang.ClassLoader)  
org.springframework.boot.logging.LoggingSystemFactory.fromSpringFactories  

spring.factories :  
```
# Logging Systems
org.springframework.boot.logging.LoggingSystemFactory=\
org.springframework.boot.logging.logback.LogbackLoggingSystem.Factory,\
org.springframework.boot.logging.log4j2.Log4J2LoggingSystem.Factory,\
org.springframework.boot.logging.java.JavaLoggingSystem.Factory

```

# Reference 
1. spring boot Actuator原理详解之启动
https://blog.csdn.net/weixin_38308374/article/details/114273724

2. Spring Boot Actuator
https://howtodoinjava.com/spring-boot/actuator-endpoints-example/