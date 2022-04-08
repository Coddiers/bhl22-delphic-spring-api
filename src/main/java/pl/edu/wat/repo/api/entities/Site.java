package pl.edu.wat.repo.api.entities;


import com.mongodb.lang.NonNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class Site extends Entity {

    @NonNull
    String url;

    @Builder.Default
    List<String> pictureIds = new ArrayList<>();

    @Builder.Default
    List<String> videoIds = new ArrayList<>();


    @Builder.Default
    List<String> textIds = new ArrayList<>();

    Instant verifiedDate;
}
