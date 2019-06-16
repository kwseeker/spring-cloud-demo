package top.kwseeker.feign.api.hystrix;

import top.kwseeker.feign.api.domain.Person;
import top.kwseeker.feign.api.service.PersonService;

import java.util.Collection;
import java.util.Collections;

public class PersonServiceFallback implements PersonService {

    @Override
    public boolean save(Person person) {
        return false;
    }

    @Override
    public Collection<Person> findAll() {
        return Collections.emptyList();
    }
}
