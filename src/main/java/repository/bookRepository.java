package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import model.Book;

@Repository
@Service 
public interface bookRepository extends CrudRepository<Book, Integer> {

}

