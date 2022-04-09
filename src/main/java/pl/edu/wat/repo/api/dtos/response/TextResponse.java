package pl.edu.wat.repo.api.dtos.response;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.repo.api.entities.Text;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TextResponse {

    String id;
    String value;
    Instant createDate;
    Boolean verified;
    Instant verifiedDate;
    Boolean fake;

    public static TextResponse from(Text text) {
        return new TextResponse(
                text.getId(),
                text.getValue(),
                text.getCreateDate(),
                text.getVerified(),
                text.getVerifiedDate(),
                text.getFake()
        );
    }
}
