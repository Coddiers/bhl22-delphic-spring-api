package pl.edu.wat.repo.api.dtos.response;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.repo.api.entities.Picture;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PictureResponse {

    String id;
    String pictureFileId;
    Instant createDate;
    Boolean verified;
    Boolean fake;
    List<String> pictureIDs;

    public static PictureResponse from(Picture picture) {
        return new PictureResponse(
                picture.getId(),
                picture.getPictureFileId(),
                picture.getCreateDate(),
                picture.getVerified(),
                picture.getFake(),
                picture.getResponsePictureFileIds()
        );
    }
}
