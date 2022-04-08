package pl.edu.wat.repo.api.entities;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
public class Video extends Entity {
    @NonNull
    String videoFileId;

    @Builder.Default
    Boolean verified = false;

    @Builder.Default
    Boolean fake = false;

    @Builder.Default
    List<String> picturesIds = new ArrayList<>();

    Instant verifiedDate;
}
