package pl.edu.wat.repo.api.dtos.response;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.repo.api.entities.Video;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VideoResponse {

    String id;
    String videoFileId;
    Instant createDate;
    Boolean verified;
    Boolean fake;
    List<String> pictureIDs;

    public static VideoResponse from(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getVideoFileId(),
                video.getCreateDate(),
                video.getVerified(),
                video.getFake(),
                video.getResponsePictureFileIds()
        );
    }
}
