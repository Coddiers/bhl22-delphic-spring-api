package pl.edu.wat.repo.api.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.repo.api.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    //    List<UserEntity> findAllBySurnameContainsOrNameContainsOrPeselContainsOrEmailContains(String surname, String name, String pesel, String email);
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
