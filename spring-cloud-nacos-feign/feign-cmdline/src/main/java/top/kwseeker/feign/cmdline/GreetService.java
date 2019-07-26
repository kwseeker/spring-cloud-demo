package top.kwseeker.feign.cmdline;

import feign.Param;
import feign.RequestLine;

/**
 * 远程服务接口定义
 */
public interface GreetService {

    @RequestLine("GET /greet/{name}/hello")
    String greetHello(@Param("name") String name);

}
