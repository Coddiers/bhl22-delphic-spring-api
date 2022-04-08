package pl.edu.wat.repo.api.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.repo.api.entities.Text;

@Repository
public interface TextRepository extends MongoRepository<Text, String> {
    Optional<Text> findFirstByVerifiedOrderByCreateDate(Boolean verified);
}
