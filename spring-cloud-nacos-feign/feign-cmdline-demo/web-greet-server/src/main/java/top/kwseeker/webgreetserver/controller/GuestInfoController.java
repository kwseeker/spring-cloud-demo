package top.kwseeker.webgreetserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.feign.common.api.domain.GuestInfo;
import top.kwseeker.feign.common.api.domain.RequestMsg;

@RestController
public class GuestInfoController {

    //模拟通过用户Id，查询用户信息
    @PostMapping("/guest/info")
    public GuestInfo getGuestInfo(RequestMsg<Integer> requestMsg) {
        GuestInfo guestInfo = new GuestInfo();
        guestInfo.setGuestId(requestMsg.getData());
        guestInfo.setName("Arvin");
        guestInfo.setMobile("13722225555");
        guestInfo.setAddress("深圳市南山区");
        return guestInfo;
    }
}
