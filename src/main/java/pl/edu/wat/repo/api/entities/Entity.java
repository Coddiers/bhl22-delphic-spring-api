package pl.edu.wat.repo.api.entities;


import com.mongodb.lang.NonNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Entity {
    @Id
    String id;

    @NonNull
    Instant createDate = Instant.now();
}
