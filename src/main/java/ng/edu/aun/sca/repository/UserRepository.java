package ng.edu.aun.sca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ng.edu.aun.sca.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByFirstnameAndLastname(String firstname, String lastname);

    List<User> findAll();

    void deleteById(Long id);

    Optional<User> findById(Long id);

    User findByVerificationToken(String token);

    List<User> findByRolesEmpty();
}
