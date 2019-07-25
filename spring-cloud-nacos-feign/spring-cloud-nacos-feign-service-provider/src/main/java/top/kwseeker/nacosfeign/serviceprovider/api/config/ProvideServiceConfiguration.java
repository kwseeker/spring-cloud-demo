package top.kwseeker.nacosfeign.serviceprovider.api.config;

import feign.Contract;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProvideServiceConfiguration {

    //@Bean
    //public Contract feignContract() {
    //    return new feign.Contract.Default();
    //}

    //@Bean
    //public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    //    return new BasicAuthRequestInterceptor("user", "password");
    //}
}
