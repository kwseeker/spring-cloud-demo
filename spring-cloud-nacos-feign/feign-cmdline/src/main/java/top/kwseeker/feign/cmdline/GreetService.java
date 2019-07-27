package top.kwseeker.feign.cmdline;

import feign.Param;
import feign.RequestLine;

/**
 * 远程服务接口定义
 */
public interface GreetService {

    //添加正则表达式对传参值进行限定
    @RequestLine("GET /greet/{name:[a-zA-Z]*}/hello")
    String greetHello(@Param("name") String name);

}
