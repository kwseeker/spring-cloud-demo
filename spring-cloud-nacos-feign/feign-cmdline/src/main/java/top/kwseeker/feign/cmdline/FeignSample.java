package top.kwseeker.feign.cmdline;

import feign.Feign;
import feign.gson.GsonDecoder;
import top.kwseeker.feign.cmdline.api.GreetService;
import top.kwseeker.feign.cmdline.api.domain.DailyNews;

public class FeignSample {

    public static void main(String[] args) {
        //生成Feign接口代理对象
        GreetService greetService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GreetService.class, "http://localhost:8083");
        //使用生成的RestTemplate发送请求
        //String message = greetService.greetHello("Lee");
        //System.out.println("Request basic service return message: " + message);
        DailyNews dailyNews = greetService.pullDailyNews();
        System.out.println(dailyNews.toString());
    }
}
