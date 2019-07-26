package top.kwseeker.feign.cmdline;

import feign.Feign;
import feign.gson.GsonDecoder;

public class FeignSample {

    public static void main(String[] args) {
        //生成RestTemplate
        GreetService greetService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GreetService.class, "http://localhost:8083");
        //使用生成的RestTemplate发送请求
        String message = greetService.greetHello("Lee");
        System.out.println("Request basic service return message: " + message);
    }
}
