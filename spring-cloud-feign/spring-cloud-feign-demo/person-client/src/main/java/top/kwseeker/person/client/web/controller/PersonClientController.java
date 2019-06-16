package top.kwseeker.person.client.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.feign.api.domain.Person;
import top.kwseeker.feign.api.service.PersonService;

import java.util.Collection;

@RestController
public class PersonClientController implements PersonService {

    private final PersonService personService;

    @Autowired
    public PersonClientController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean save(@RequestBody Person person) {   //接口参数属性无法传递
        return personService.save(person);
    }

    @Override
    public Collection<Person> findAll() {
        return personService.findAll();
    }
}
