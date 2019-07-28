# Feign

Feign是个很轻量的工具，代码量也很少。

[Feign github repo](https://github.com/OpenFeign/feign/blob/master/README.md)

是否可以将Feign归类为RPC？

## 1 Feign 简介

Feign makes writing java http clients easier!

Feign 就是自动包装 Http客户端，整合其他组件 生成简单易用统一
可通过命名路由服务等等功能的工具。

#### 组件集成

+ 编解码器(Encoder Decoder)
    - Gson
    - Jackson
    - Sax(XML)
    - JAXB(XML)
    - SOAP(包含编解码功能，不只是编解码器)
+ 注解处理条约
    - JAX-RS（默认）
+ Http Client
    - OkHttp
    - Ribbon
    - Http2Client
+ 断路器
    - Hystrix
+ 日志
    - SLF4J

#### 功能

+ 组件定制

+ 多接口

## 2 Feign 原理

#### Feign 原理简介

##### 关于为什么要了解Feign工作原理

因为知道了Feign的作用,学几个demo，并不能做到正确使用Feign。还要了解工作原理，了解Feign在一个工程项目的层次所属，以及作用的对象。有一个清晰的认识，进而写出最合理的代码，不至于出现像刚学习时乱加注解，代码到处乱放的情况。

#### Feign 原理源码分析

```
GreetService greetService = Feign.builder()
        .decoder(new GsonDecoder())
        .target(GreetService.class, "http://localhost:8083");
String message = greetService.greetHello("Lee");
```
1. 组件装配 Feign$Builder
    
    1.1 Feign.builder() 组装默认的组件到Feign$Builder中。

    + requestInterceptors(RequestInterceptor)
        
        一组请求拦截器，ArrayList类型。RequestInterceptor
        是拦截器接口，内部有个apply()方法表示将此拦截器应用到哪个RequestTemplate（后面讲这个类）。
        
    + logLevel(Logger.Level)
        
        日志级别，默认NONE不打印Feign的任何日志，
        还有BASIC、HEADERS、FULL。
    
    + constract(Constract)
    
        单词为“合约”的意思，Constract$Default 是默认的实现。  
        实际上是注解处理器，处理注释在类、方法、参数上的注解。可以先在各个处理器方法上面加上断点，然后在测试代码加不同类型注解，debug，研究Feign注解处理原理。
        
        在下面 ReflectiveFeign 实例化中应用到。
        
        Feign接口注解：
        
        | Annotation     | Interface Target | Usage |
        |----------------|------------------|-------|
        | `@RequestLine` | Method           | 为请求的API或者请求的客户端设置请求头。 Defines the `HttpMethod` and `UriTemplate` for request.  `Expressions`, values wrapped in curly-braces `{expression}` are resolved using their corresponding `@Param` annotated parameters. |
        | `@Param`       | Parameter        | Defines a template variable, whose value will be used to resolve the corresponding template `Expression`, by name. |
        | `@Headers`     | Method, Type     | Defines a `HeaderTemplate`; a variation on a `UriTemplate`.  that uses `@Param` annotated values to resolve the corresponding `Expressions`.  When used on a `Type`, the template will be applied to every request.  When used on a `Method`, the template will apply only to the annotated method. |
        | `@QueryMap`    | Parameter        | Defines a `Map` of name-value pairs, or POJO, to expand into a query string. |
        | `@HeaderMap`   | Parameter        | Defines a `Map` of name-value pairs, to expand into `Http Headers` |
        | `@Body`        | Method           | Defines a `Template`, similar to a `UriTemplate` and `HeaderTemplate`, that uses `@Param` annotated values to resolve the corresponding `Expressions`.|

        - MethodMetadata
        
            方法元数据，如方法返回值类型，方法名，方法传参等；用于动态生成http请求模版。
    
    + client(Client)
    
        Client是发送请求接收返回的接口，Client$Default是
        Feign提供的默认的实现，Http请求与接收返回就是在Client的实现类中做的。  
        支持手动指定SSLSocketFactory和HostnameVerifier。
        
    + retryer(Retryer)
    
        Retryer是请求重试接口，Retryer$Default是默认的实现，可以指定重试周期，次数。  
        NEVER_RETRY 也是此接口的static实现，是不重试请求。
    
    + logger(Logger)
        
        Feign自己实现的Logger组件抽象类。NoOpLogger是默认实现，即不打印任何日志，feign-core还提供了ErrorLogger、JavaLogger，feign-slf4j提供了Slf4JLogger实现基于Slf4j日志框架。
        
    + ecoder(Encoder)
        
        Encoder是编码器接口，Encoder$Default是其默认实现，只能转换字符串和字节数组。Feign的拓展组件提供了基于Gson、Jackson等的实现。Spring整合Feign时也有提供
        Encoder的实现。
    
    + decoder(Decoder)
        
        同上。
    
    + queryMapEncoder(QueryMapEncoder)
    
    + errorDecoder(ErrorDecoder)
    
    + options(Request$Options)
    
        请求配置：连接超时、读取超时、是否跟踪重定向。
    
    + invocationHandlerFactory(InvocationHandlerFactory)  
    
        调用处理器（InvocationHandler）的创建工厂；  
        InvocationHandler将请求分发给反射请求发送方法。  
        InvocationHandlerFactory$Default是其默认实现，创建
        FeignInvocationHandler类型调用处理器。  
        MethodHandler是请求分发后调用的请求接口。  
    
    + ExceptionPropagationPolicy
       
    1.2 可以使用setter方法自行定制组件的实现，覆盖默认的组件。
        
2. 生成请求模版

    ```java
    public <T> T target(Class<T> apiType, String url) {
      return target(new HardCodedTarget<T>(apiType, url));
    }

    public <T> T target(Target<T> target) {
      return build().newInstance(target);
    }

    //实例化ReflectivceFeign客户端
    public Feign build() {
      //实例化SynchronousMethoHandler工厂
      SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
          new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors, logger,
              logLevel, decode404, closeAfterDecode, propagationPolicy);
      //
      ParseHandlersByName handlersByName =
          new ParseHandlersByName(contract, options, encoder, decoder, queryMapEncoder,
              errorDecoder, synchronousMethodHandlerFactory);
      
      return new ReflectiveFeign(handlersByName, invocationHandlerFactory, queryMapEncoder);
    }
    ```
    
    + HardCodedTarget http请求信息封装类
    
        包含请求接口类型(Class)，请求名，请求Url。
        
    + 构建Feign接口代理对象
    
        - ReflectiveFeign
        
            Feign是抽象类，ReflectiveFeign是官方唯一实现类。  
            
            * ParseHandlersByname
                
                通过名称解析处理器，猜想这个对象是通过名称获取SynchronousMethodHandler的。
                
            * InvocationHandlerFactory
            
            * QueryMapEncoder
            
            ReflectiveFeign生成Feign接口代理对象过程：
            
            ```java
            public <T> T newInstance(Target<T> target) {
                //首先对请求接口类泛型参数进行检查，不支持泛型接口类；
                //然后检查接口方法数，只支持不继承其他接口的接口或者单继承的接口；继承的接口同样需要遵循此规则；
                //获取方法元数据MethodMetadata存储到LinkedHashMap（不处理Object类方法、static方法，Default方法（详见Feign源码））；
                //  这一步用到第一步创建的 Contract$Default 实例;
                //  获取的方法元数据包括 方法返回类型、configKey接口名和方法及参数;
                //  然后是依次处理类上的注解、方法上的注解、参数上的注解，详细过程查看《feign注解处理》小节。
                //  为每个方法创建模版，使用模版和MethodHandler工厂创建每个方法的SynchronousMethodHandler，最终返回接口方法名与SynchrousMethodHandler相对应的Map。
                Map<String, MethodHandler> nameToHandler = targetToHandlersByName.apply(target);
                
                Map<Method, MethodHandler> methodToHandler = new LinkedHashMap<Method, MethodHandler>();
                List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();
                //使用nameToHandler建立起Method对MethodHandler的对应关系 methodToHandler
                for (Method method : target.type().getMethods()) {
                  if (method.getDeclaringClass() == Object.class) {
                    continue;
                  } else if (Util.isDefault(method)) {
                    DefaultMethodHandler handler = new DefaultMethodHandler(method);
                    defaultMethodHandlers.add(handler);
                    methodToHandler.put(method, handler);
                  } else {
                    methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
                  }
                }
                //默认生成ReflectivceFeign.FeignInvocationHandler, 远程调用的url、name、Feign客户端接口，
                //以及内部方法调用处理器列表(即请求分发处理器dispatcher）
                InvocationHandler handler = factory.create(target, methodToHandler);
                //生成代理对象，并返回
                T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(),
                    new Class<?>[] {target.type()}, handler);
            
                for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
                  defaultMethodHandler.bindTo(proxy);
                }
                return proxy;
              }
            ```
    
        - SynchronousMethodHandler
        
            名字上是“同步方法处理器”，猜想应该是处理controller方法调用的；比如Demo中`String message = greetService.greetHello("Lee");`, 那么SynchronousMethodHandler应该就是处理`greetHello()`这个方法调用的。
        
            * Factory
                
                创建SynchronousMethodHandler的工厂，读取Feign$Builder的组件，创建SynchronousMethodHandler实例。
            
            

3. 发送Http请求

    通过代理对象请求接口的方法。代理对象会调用其实际处理类复写的InvocationHandler方法invoke();交给FeignInovationHandler.dispatch 匹配到方法及相应的MethodHandler 并调用处理方法，请求模版是代理类调用接口方法后才临时生成的。

    ```
    public RequestTemplate create(Object[] argv) {
      RequestTemplate mutable = RequestTemplate.from(metadata.template());
      if (metadata.urlIndex() != null) {
        int urlIndex = metadata.urlIndex();
        checkArgument(argv[urlIndex] != null, "URI parameter %s was null", urlIndex);
        mutable.target(String.valueOf(argv[urlIndex]));
      }
      Map<String, Object> varBuilder = new LinkedHashMap<String, Object>();
      for (Entry<Integer, Collection<String>> entry : metadata.indexToName().entrySet()) {
        int i = entry.getKey();
        Object value = argv[entry.getKey()];
        if (value != null) { // Null values are skipped.
          if (indexToExpander.containsKey(i)) {
            value = expandElements(indexToExpander.get(i), value);
          }
          for (String name : entry.getValue()) {
            varBuilder.put(name, value);
          }
        }
      }

      RequestTemplate template = resolve(argv, mutable, varBuilder);
      if (metadata.queryMapIndex() != null) {
        // add query map parameters after initial resolve so that they take
        // precedence over any predefined values
        Object value = argv[metadata.queryMapIndex()];
        Map<String, Object> queryMap = toQueryMap(value);
        template = addQueryMapQueryParameters(queryMap, template);
      }

      if (metadata.headerMapIndex() != null) {
        template =
            addHeaderMapHeaders((Map<String, Object>) argv[metadata.headerMapIndex()], template);
      }

      return template;
    }
    ```

    ```
    public Object invoke(Object[] argv) throws Throwable {
        //即上面生成http请求模版
        RequestTemplate template = buildTemplateFromArgs.create(argv);
        //克隆一个重试器，用于请求异常重试
        Retryer retryer = this.retryer.clone();
        while (true) {
          try {
            //执行并对返回结果进行解码，这里面应用到解码器
            return executeAndDecode(template);
          } catch (RetryableException e) {
            try {
              retryer.continueOrPropagate(e);
            } catch (RetryableException th) {
              Throwable cause = th.getCause();
              if (propagationPolicy == UNWRAP && cause != null) {
                throw cause;
              } else {
                throw th;
              }
            }
            if (logLevel != Logger.Level.NONE) {
              logger.logRetry(metadata.configKey(), logLevel);
            }
            continue;
          }
        }
    }
    ```
    
    生成请求实例，发送请求，对请求结果进行解码
    ```
    Object executeAndDecode(RequestTemplate template) throws Throwable {
        Request request = targetRequest(template);
    
        if (logLevel != Logger.Level.NONE) {
          logger.logRequest(metadata.configKey(), logLevel, request);
        }
    
        Response response;
        long start = System.nanoTime();
        try {
          response = client.execute(request, options);
        } catch (IOException e) {
          if (logLevel != Logger.Level.NONE) {
            logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime(start));
          }
          throw errorExecuting(request, e);
        }
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    
        boolean shouldClose = true;
        try {
          if (logLevel != Logger.Level.NONE) {
            response =
                logger.logAndRebufferResponse(metadata.configKey(), logLevel, response, elapsedTime);
          }
          if (Response.class == metadata.returnType()) {
            if (response.body() == null) {
              return response;
            }
            if (response.body().length() == null ||
                response.body().length() > MAX_RESPONSE_BUFFER_SIZE) {
              shouldClose = false;
              return response;
            }
            // Ensure the response body is disconnected
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            return response.toBuilder().body(bodyData).build();
          }
          if (response.status() >= 200 && response.status() < 300) {
            if (void.class == metadata.returnType()) {
              return null;
            } else {
              Object result = decode(response);
              shouldClose = closeAfterDecode;
              return result;
            }
          } else if (decode404 && response.status() == 404 && void.class != metadata.returnType()) {
            Object result = decode(response);
            shouldClose = closeAfterDecode;
            return result;
          } else {
            throw errorDecoder.decode(metadata.configKey(), response);
          }
        } catch (IOException e) {
          if (logLevel != Logger.Level.NONE) {
            logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime);
          }
          throw errorReading(request, response, e);
        } finally {
          if (shouldClose) {
            ensureClosed(response.body());
          }
        }
    }
    ```

##### Feign注解处理

这部分相对较为复杂所以单独拿出来分析。

```java
protected MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
    MethodMetadata data = new MethodMetadata();
    data.returnType(Types.resolve(targetType, targetType, method.getGenericReturnType()));
    data.configKey(Feign.configKey(targetType, method));

    if (targetType.getInterfaces().length == 1) {
        processAnnotationOnClass(data, targetType.getInterfaces()[0]);
    }
    
    //处理接口类上的注解，只有 @Headers
    //  如果类上注有@Headers则读取注解内容（String[], 内容为空抛异常）
    //  请求头传参都是键值对（如：Content-Type:text/html），Feign将注解的值按键值对读取到 LinkedHashMap 中。
    //  然后以附加的方式整合到 MethodMetadata$RequestTemplate$Map<String, HeaderTemplate> headers中
    processAnnotationOnClass(data, targetType);

    //遍历接口类中的方法，处理方法上的注解，有三种 @RequestLine @Body @Headers
    //  RequestLine 注解解析：
    //      1）读取 value 值
    //      2）请求行格式校验 ^([A-Z]+)[ ]*(.*)$ 一或多个A-Z字母开头加0或1或多个空格加任意0或1或多个字符
    //      3）解析 value 值中的 请求类型 和 路径 赋值到 RequestTemplate method、url 中
    //      4）解析 decodeSlash、collectionFormat 值同样赋值到 RequestTemplate 中；
    //  Body 注解解析：
    //      
    //  Headers 注解解析：
    //      将内容全部以键值对的方式解析赋值到 RequestTemplate headers 成员中。
    for (Annotation methodAnnotation : method.getAnnotations()) {
        processAnnotationOnMethod(data, methodAnnotation, method);
    }
    checkState(data.template().method() != null,
        "Method %s not annotated with HTTP method type (ex. GET, POST)",
        method.getName());
    //获取类参数类型和泛型参数类型
    Class<?>[] parameterTypes = method.getParameterTypes();
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    
    //获取方法参数注解并解析：
    //  Param注解：
    //      获取方法参数名全部存入 Feign 合约 Contract$MethodMetadata 中。
    //      解析 expander 参数，都是读取注解内容赋值给合约的元数据
    //      解析 encode 参数
    //  QueryMap注解：
    //
    //  HeaderMap注解：
    //
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    int count = parameterAnnotations.length;
    for (int i = 0; i < count; i++) {
        boolean isHttpAnnotation = false;
        if (parameterAnnotations[i] != null) {
            isHttpAnnotation = processAnnotationsOnParameter(data, parameterAnnotations[i], i);
        }
        if (parameterTypes[i] == URI.class) {
          data.urlIndex(i);
        } else if (!isHttpAnnotation) {
          checkState(data.formParams().isEmpty(),
              "Body parameters cannot be used with form parameters.");
          checkState(data.bodyIndex() == null, "Method has too many Body parameters: %s", method);
          data.bodyIndex(i);
          data.bodyType(Types.resolve(targetType, targetType, genericParameterTypes[i]));
        }
    }

    if (data.headerMapIndex() != null) {
    checkMapString("HeaderMap", parameterTypes[data.headerMapIndex()],
        genericParameterTypes[data.headerMapIndex()]);
    }

    if (data.queryMapIndex() != null) {
        if (Map.class.isAssignableFrom(parameterTypes[data.queryMapIndex()])) {
            checkMapKeys("QueryMap", genericParameterTypes[data.queryMapIndex()]);
        }
    }

    return data;
}
```

#### Spring Cloud 整合 Feign 原理


## 3 Feign 开发

#### 心得总结

1）可以从Feign.Builder一窥究竟，个人认为很多框架都包含一个总概全篇的东西，从中可以看到框架包含哪些组件，组件依赖初始化先后顺序等等信息。此工具中的Builder，大型框架的上下文。

从Feig.Builder中可以看到这些组件，学完Feign源码后可以回来看看是不是在这里统构全篇的。
```
private final List<RequestInterceptor> requestInterceptors =
        new ArrayList<RequestInterceptor>();
private Logger.Level logLevel = Logger.Level.NONE;
private Contract contract = new Contract.Default();
private Client client = new Client.Default(null, null);
private Retryer retryer = new Retryer.Default();
private Logger logger = new NoOpLogger();
private Encoder encoder = new Encoder.Default();
private Decoder decoder = new Decoder.Default();
private QueryMapEncoder queryMapEncoder = new QueryMapEncoder.Default();
private ErrorDecoder errorDecoder = new ErrorDecoder.Default();
private Options options = new Options();
private InvocationHandlerFactory invocationHandlerFactory =
    new InvocationHandlerFactory.Default();
private boolean decode404;
private boolean closeAfterDecode = true;
private ExceptionPropagationPolicy propagationPolicy = NONE;
```

2）看源码除了理解整个数据流流程还要清楚某个功能组件在哪里被初始化，在哪里被调用的。
然后出现因自己使用不当（很多细节肯定没有时间研究清楚，很可能使用相关功能时会报异常），
然后自己可以立刻知道自己应该去看框架哪部分源码排查错误。比如初学Feign时，配置了Gson
解码器，但是远程接口返回的是String。就报了Gson解码异常的错误。经过



#### 编程基础

+ 接口注解(条约contract的一部分)
    
    - @RequestLine  
    
        Http请求行定义，请求行参数在"{}"中定义，由@Param处理。
    
    - @Param
        
        处理请求行参数的，Spring MVC 中为 @PathVariable。
        
    - @Headers
    
        定义HeaderTemplate(UriTemplate的变体)。可以作用于类上（类中所有方法有效）或方法上。        
    - @QueryMap
        
        使用键值对传递请求参数。        

    - @HeaderMap
    
        使用键值对传递Header参数。
        
    - @Body

#### 功能定制



## 附录

+ java.lang.Class 

    - getTypeParameters()
    
        ```
        public TypeVariable<Class<T>>[] getTypeParameters()
        //Returns an array of TypeVariable objects that represent the type variables declared by the generic declaration represented by this GenericDeclaration object, in declaration order. 
        //Returns an array of length 0 if the underlying generic declaration declares no type variables.
        ```
        此方法用于获取 Class 对象的泛型参数（返回数组）。
        
    - getSimpleName() 
    
        ```
        public String getSimpleName()
        ```
        此方法用于获取 Class 对象的简写名称，如 "top.kwseeker.feign.cmdline.GreetService"的简名是"GreetService"。
        
    - getInterfaces()
    
        此方法获取类实现的接口（返回数组），或接口继承的接口（返回数组）。

+ java.lang.Method

    - getDeclaringClass() 
    
        ```
        public Class<?> getDeclaringClass()  
        Returns the Class object representing the class or interface that declares the executable represented by this object.
        ```
        获取声明此方法的接口或者类。
        
    - getModifiers()
    
        ```
        int	getModifiers()
        Returns the Java language modifiers for the member or constructor represented by this Member, as an integer.
        ```
        返回此类或接口以整数编码的 Java 语言修饰符。
        
        Java语言修饰符详见Modifier类。
        ```
        public static final int PUBLIC  = 0x00000001;
        public static final int PRIVATE  = 0x00000002;
        public static final int PROTECTED  = 0x00000004;
        public static final int STATIC  = 0x00000008;
        public static final int FINAL  = 0x00000010;
        public static final int SYNCHRONIZED  = 0x00000020;        同步
        public static final int VOLATILE  = 0x00000040;        用volatile修饰的变量，线程在每次使用变量的时候，都会读取变量修改后的最的值。
        public static final int TRANSIENT  = 0x00000080;        用transient关键字标记的成员变量不参与序列化过程。
        public static final int NATIVE  = 0x00000100;
        public static final int INTERFACE  = 0x00000200;
        public static final int ABSTRACT  = 0x00000400;
        public static final int STRICT  = 0x00000800;        即strictfp(strict float point 精确浮点)，此关键字可应用于类、接口或方法。
        ```

+ Matcher.group()
    
    group是针对（）来说的，group（0）就是指的整个串，group（1） 指的是第一个括号里的东西，group（2）指的第二个括号里的东西。

    Feign请求行正则表达式 `static final Pattern REQUEST_LINE_PATTERN = Pattern.compile("^([A-Z]+)[ ]*(.*)$");` 对于 传参 "GET /greet/{name:[a-zA-Z]*}/hello", 则 Matcher.group(1) 指的是([A-Z]+)配到的字符串 "GET", Matcher.group(2) 指的是 "/greet/{name:[a-zA-Z]*}/hello"