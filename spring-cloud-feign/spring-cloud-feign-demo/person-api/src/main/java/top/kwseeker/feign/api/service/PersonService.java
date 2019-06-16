package top.kwseeker.feign.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.kwseeker.feign.api.domain.Person;
import top.kwseeker.feign.api.hystrix.PersonServiceFallback;

import java.util.Collection;

@FeignClient(value="person-service", fallback = PersonServiceFallback.class)
public interface PersonService {

    @PostMapping(value = "/person/save")
    boolean save(@RequestBody Person person);

    @GetMapping(value = "/person/find/all")
    Collection<Person> findAll();
}
