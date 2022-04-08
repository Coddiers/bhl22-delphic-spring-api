package pl.edu.wat.repo.api.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.repo.api.entities.Picture;

@Repository
public interface PictureRepository extends MongoRepository<Picture, String> {
    Optional<Picture> findFirstByVerifiedOrderByCreateDate(Boolean verified);
}
