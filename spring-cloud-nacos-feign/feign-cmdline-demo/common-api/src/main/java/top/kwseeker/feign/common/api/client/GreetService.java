package top.kwseeker.feign.common.api.client;

import feign.Headers;
import feign.RequestLine;
import top.kwseeker.feign.common.api.domain.DailyNews;

/**
 * 远程服务接口定义
 */
public interface GreetService {

    //添加正则表达式对传参值进行限定
    //@Headers("Content-Type:text/plain")
    //@RequestLine(value = "GET /greet/{name:[a-zA-Z]*}/hello",
    //        decodeSlash = true,
    //        collectionFormat = CollectionFormat.EXPLODED)
    //String greetHello(@Param("name") String name);

    @Headers("Content-Type:application/json")
    @RequestLine("GET /greet/dailynews")
    DailyNews pullDailyNews();
}
