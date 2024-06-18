package springProject.DAO;

import com.example.demo.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {
    private final RowMapper rowMapperPerson;
    private final RowMapper rowMapperBook;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(RowMapper rowMapperPerson, RowMapper rowMapperBook, JdbcTemplate jdbcTemplate) {
        this.rowMapperPerson = rowMapperPerson;
        this.rowMapperBook = rowMapperBook;
        this.jdbcTemplate = jdbcTemplate;
    }


    //Этот метод вытаскивает год и айди не правильно
    public List<Person> showAll(){   //Этот метод вытаскивает год и айди не правильно(присваивает им значения по-умолчанию-0) из-за BeanPropertyRowMapper<>(Person.class) - узнай из-за чего так происходит!!!!
        //Так я узнал что во всём виноват  BeanPropertyRowMapper
        */
/*List<Person> people = jdbcTemplate.query("SELECT * FROM Person",new BeanPropertyRowMapper<>(Person.class));
        System.out.println("showAll");
        people.forEach(p-> System.out.println(p.getFullName()+", "+p.getYear()+", "+p.getId()));*//*

        return jdbcTemplate.query("SELECT * FROM Person",rowMapperPerson);
    }
    public Person show(int id){
        return jdbcTemplate.query("SELECT * FROM Person WHERE person_id=?",new Object[]{id},rowMapperPerson).
                stream().findAny().orElse(null);
    }
    public void save(Person person){
        jdbcTemplate.update("INSERT INTO Person(full_name,year_of_birth) VALUES(?,?)",person.getFullName(),person.getYear());
    }
    public void update(int id,Person updatedPerson){
        jdbcTemplate.update("UPDATE Person SET full_name=?,year_of_birth=? WHERE person_id=?",updatedPerson.getFullName(),updatedPerson.getYear(),id);
    }
    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Person WHERE person_id=?",id);
    }

    //метод для проверки уникальности fullName в методе validate
    public Person show(String fullName){
        return jdbcTemplate.query("SELECT * FROM Person WHERE full_name=?",new Object[]{fullName},rowMapperPerson).stream().findAny().orElse(null);
    }
    //Метод для того чтобы вытащить книги которые находятся у человека с переданным id
    public List<Book> getBooksByPersonId(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?",new Object[]{id},rowMapperBook);
    }
}
