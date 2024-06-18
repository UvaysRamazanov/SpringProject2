package springProject.repositories;

import com.example.demo.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepositories extends JpaRepository<Person,Integer> {
    Person findByFullName(String fullName);
}