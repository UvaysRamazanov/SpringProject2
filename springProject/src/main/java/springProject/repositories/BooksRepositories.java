package springProject.repositories;


import com.example.demo.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepositories extends JpaRepository<Book,Integer>, PagingAndSortingRepository<Book,Integer> {
    List<Book> findByNameStartingWith(String name);
}