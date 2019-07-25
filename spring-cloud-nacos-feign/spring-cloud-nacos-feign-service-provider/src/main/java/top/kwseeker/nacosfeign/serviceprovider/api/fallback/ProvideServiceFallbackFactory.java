package top.kwseeker.nacosfeign.serviceprovider.api.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import top.kwseeker.nacosfeign.serviceprovider.api.ProvideServiceApi;

@Component
public class ProvideServiceFallbackFactory implements FallbackFactory<ProvideServiceApi> {

    @Override
    public ProvideServiceApi create(Throwable cause) {
        return new ProvideServiceApi() {
            @Override
            public String greet() {
                return "fallback; reason was:" + cause.getMessage();
            }
        };
    }

}
