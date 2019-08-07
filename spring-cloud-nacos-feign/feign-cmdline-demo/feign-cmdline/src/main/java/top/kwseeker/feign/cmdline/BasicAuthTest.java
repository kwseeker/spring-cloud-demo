package top.kwseeker.feign.cmdline;

import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import top.kwseeker.feign.common.api.client.GuestInfoService;
import top.kwseeker.feign.common.api.domain.GuestInfo;
import top.kwseeker.feign.common.api.domain.RequestMsg;

public class BasicAuthTest {

    public static void main(String[] args) {
        GuestInfoService guestInfoService = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor("Arvin", "123456"))
                .target(GuestInfoService.class, "http://localhost:8083");
        GuestInfo guestInfo = guestInfoService.getGuestInfo(new RequestMsg<>(1000001));
        System.out.println(guestInfo.toString());
    }
}
