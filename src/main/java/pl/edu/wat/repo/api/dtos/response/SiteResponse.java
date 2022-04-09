package pl.edu.wat.repo.api.dtos.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.repo.api.entities.Site;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SiteResponse {

    String url;
    List<String> textIds;
    List<String> videoIds;
    List<String> pictureIds;
    Boolean fake;
    Boolean verified;

    public static SiteResponse from(Site site, Boolean fake, Boolean verified) {
        return new SiteResponse(
                site.getUrl(),
                site.getTextIds(),
                site.getVideoIds(),
                site.getPictureIds(),
                fake,
                verified
        );
    }
}
