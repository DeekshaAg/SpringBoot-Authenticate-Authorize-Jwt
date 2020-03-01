package io.springpractice.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import io.springpractice.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>{
	
	Optional<User> findByUserName(String userName);
}
