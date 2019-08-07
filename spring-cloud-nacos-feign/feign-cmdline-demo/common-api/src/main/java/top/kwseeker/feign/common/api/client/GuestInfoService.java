package top.kwseeker.feign.common.api.client;

import feign.Headers;
import feign.RequestLine;
import top.kwseeker.feign.common.api.domain.GuestInfo;
import top.kwseeker.feign.common.api.domain.RequestMsg;

public interface GuestInfoService {

    //@Headers("Authorization: Basic QXJ2aW46MTIzNDU2")
    @RequestLine("POST /guest/info")
    public GuestInfo getGuestInfo(RequestMsg<Integer> requestMsg);
}
