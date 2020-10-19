package example;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class PersonService implements InitializingBean {
    @Autowired
    PersonService self;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonInfoRepository personInfoRepository;
    @Autowired
    EntityManager entityManager;

    @Transactional
    public void transactionalMethod() {
        personRepository.getByName("test").orElse(null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        self.init();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertInit() {
        Person person = personRepository.save(new Person("asd"));
        PersonInfo personInfo = personInfoRepository.save(new PersonInfo(person));
    }

    @Transactional
    public void init() {
        List<PersonInfo> existing = personInfoRepository.findAll();
        if (existing.isEmpty())
            self.insertInit();
        entityManager.flush();
        entityManager.clear();
        personRepository.findAll();
        personInfoRepository.findAll();
    }
}
