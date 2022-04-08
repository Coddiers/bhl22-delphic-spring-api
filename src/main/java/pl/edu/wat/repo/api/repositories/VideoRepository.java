package pl.edu.wat.repo.api.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.repo.api.entities.File;
import pl.edu.wat.repo.api.entities.Video;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    Optional<Video> findFirstByVerifiedOrderByCreateDate(Boolean verified);
}
