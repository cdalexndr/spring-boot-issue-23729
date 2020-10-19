package example;

import org.springframework.cache.annotation.EnableCaching;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@EnableCaching
public class PersonInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Person person;

    public PersonInfo() {
    }

    public PersonInfo(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        PersonInfo that = (PersonInfo) o;
        return person.equals(that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person);
    }
}
