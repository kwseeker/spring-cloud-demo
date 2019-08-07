# 微服务网关

网关的功能：
+ 身份认证
+ 路由服务
+ 系统监控

从 Spring Cloud Gateway 可以学到什么？

1）通过配置定制标准化的 HandlerMapping  
对多个项目业务开发时会遇到一些样子长得总是差不多的服务接口；
应该可以参考 Gateway的实现 将 HandlerMapping 模块化，并导入到不同的项目。使用时使用几行配置引入。

## <font color="blue"> 1 Spring Cloud Gateway</font>

Spring Cloud Gateway 比 Zuul1.0 性能更好。基于 Spring Boot 2.x, Spring WebFlux, and Project Reactor 实现。
即异步事件驱动，因此对于某些同步库（如：Spring Data、
Spring Security）可能无法使用。


### Spring Cloud Gateway 原理

1）客户端向Spring Cloud Gateway发出请求。    
2）如果网关处理程序映射确定请求与路由匹配，则将其发送到网关Web处理程序。   
3）此处理程序运行通过特定于请求的过滤器链发送请求。 
4）滤波器被虚线划分的原因是滤波器可以在发送代理请求之前或之后执行逻辑。 执行所有“预”过滤器逻辑，然后进行代理请求。   
5）在发出代理请求之后，执行“post”过滤器逻辑。  

即: "匹配(Predicate)" -> "路由(Route)" -> "过滤(Filter)";
Predicate 和 Filter 是 Route 的一部分。

+ Route Predicate Factories

    Spring Cloud Gateway 将路由匹配（Predicate）作为 Spring WebFlux HandlerMapping 基础结构的一部分；
    内置了很多Route Predicate 工厂。所有这些 Predicate 适用于不同的请求场景（估计类似RabbitMQ的交换机匹配）。
    可以通过 and 合并多个 Route Predicate Factories。
    
    可以通过 application.properties 以配置的方式添加 RoutePredicateFactory 
    
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
          - id: after_route           # route 唯一ID
            uri: https://example.org  # 匹配predicates后执行的 HandlerMapping
            predicates:               # 多个 Predicate
            - After=2017-01-20T17:42:47.789-07:00[America/Denver] # 日期+时区
            - Cookie=chocolate, ch.p  # cookie名 + 正则表达式
            - Path=/foo/{segment},/bar/{segment}
            - Query=foo, ba*          # 匹配请求中包含baz请求参数的请求: 请求参数名 + 参数值的正则表达式 
            - RemoteAddr=192.168.1.1/24
    ```
    
    - 时间类 Route Predicate Factory
        * After Route Predicate Factory
        
            用于匹配某个时间点之后的所有请求
            
        * Before Route Predicate Factory
            
            用于匹配某个时间点之前的所有请求
            
        * Between Route Predicate Factory
    
    - 请求参数类 Route Predicate Factory
    
        * Cookie Route Predicate Factory
        
            使用 cookie名 和 正则表达式 匹配带有 cookie 的请求
            
        * Header Route Predicate Factory
        
            使用 header名 和 正则表达式 匹配带有 header 的请求。
        
        * Host Route Predicate Factory
        
        * Method Route Predicate Factory
        
            匹配 GET、POST 等请求方法。
                
        * Path Route Predicate Factory
        
            匹配请求路径。
            
        * Query Route Predicate Factory
        
        * RemoteAddr Route Predicate Factory
        
            默认会传递接收到的 remote address。但是如果 Gateway 使用代理的话。这个远程地址可能不正确。
            可以定制 RemoteAddressResolver 解决这个问题。
            
            提供了一个实现类 XForwardedRemoteAddressResolver，可以指定转发地址。
     
    - 负载均衡类 Route Predicate Factory 
    
        * Weight Route Predicate Factory
            
            ```yaml
            spring:
              cloud:
                gateway:
                  routes:
                  - id: weight_high
                    uri: https://weighthigh.org
                    predicates:
                    - Weight=group1, 8
                  - id: weight_low
                    uri: https://weightlow.org
                    predicates:
                    - Weight=group1, 2
            ```
            
+ GatewayFilter Factories
    
    路由下发之前的执行的过滤处理逻辑。
    
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
          - id: add_request_header_route
            uri: https://example.org
            filters:
            - AddRequestHeader=X-Request-Foo, Bar
    ```
    
    - AddRequestHeader GatewayFilter Factory
    
        对请求添加 请求头
        
    - AddResponseHeader GatewayFilter Factory
    
    - DedupeResponseHeader GatewayFilter Factory
    
        删除返回中重复的数据
        
    - Hystrix GatewayFilter Factory
    
    - FallbackHeaders GatewayFilter Factory
        
    - MapRequestHeader GatewayFilter Factory
    
    - PrefixPath GatewayFilter Factory
    
    - PreserveHostHeader GatewayFilter Factory
    
    - RequestRateLimiter GatewayFilter Factory
    
    - RedirectTo GatewayFilter Factory
    
    - RemoveHopByHopHeadersFilter GatewayFilter Factory
    
    - RemoveRequestHeader GatewayFilter Factory
    
    - RemoveResponseHeader GatewayFilter Factory

    - RemoveRequestParameter GatewayFilter Factory

    - RewritePath GatewayFilter Factory
    
    - RewriteLocationResponseHeader GatewayFilter Factory
    
    - RewriteResponseHeader GatewayFilter Factory
    
    - SaveSession GatewayFilter Factory

    - SecureHeaders GatewayFilter Factory

    - SetPath GatewayFilter Factory
    
    - SetRequestHeader GatewayFilter Factory

    - SetResponseHeader GatewayFilter Factory
    
    - SetStatus GatewayFilter Factory

    - StripPrefix GatewayFilter Factory

    - Retry GatewayFilter Factory
    
    - RequestSize GatewayFilter Factory
    
    - Modify Request Body GatewayFilter Factory
    
    - Modify Request Body GatewayFilter Factory

    - DefaultFilters

+ Global Filters
    
    - Combined Global Filter and GatewayFilter Ordering

    - Forward Routing Filter
    
    - LoadBalancerClient Filter
    
    - Netty Routing Filter
   
    - Netty Write Response Filter
    
    - RouteToRequestUrl Filter
    
    - Websocket Routing Filter
    
    - Gateway Metrics Filter
    
    - Marking An Exchange As Routed
    
+ 传输层安全协议 TLS/SSL

+ Configuration

+ Route 元数据配置

+ Reactor Netty Access Logs

+ CORS 配置

+ Actuator API



## <font color="blue"> 2 Zuul</font>

