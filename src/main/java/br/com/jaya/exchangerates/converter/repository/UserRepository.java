package br.com.jaya.exchangerates.converter.repository;

import br.com.jaya.exchangerates.converter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByApikey(String apikey);

}
