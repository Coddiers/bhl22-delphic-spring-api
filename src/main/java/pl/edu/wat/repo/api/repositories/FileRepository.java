package pl.edu.wat.repo.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.repo.api.entities.File;

@Repository
public interface FileRepository extends MongoRepository<File, String> {
}
