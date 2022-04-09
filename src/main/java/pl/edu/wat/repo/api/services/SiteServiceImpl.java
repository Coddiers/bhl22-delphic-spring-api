package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.repo.api.dtos.response.SiteResponse;
import pl.edu.wat.repo.api.entities.Picture;
import pl.edu.wat.repo.api.entities.Site;
import pl.edu.wat.repo.api.entities.Text;
import pl.edu.wat.repo.api.entities.Video;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.repositories.PictureRepository;
import pl.edu.wat.repo.api.repositories.SiteRepository;
import pl.edu.wat.repo.api.repositories.TextRepository;
import pl.edu.wat.repo.api.repositories.VideoRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SiteServiceImpl implements SiteService {

    WebScrapingServiceImpl webScrapingService;
    SiteRepository siteRepository;
    PictureRepository pictureRepository;
    TextRepository textRepository;
    VideoRepository videoRepository;

    @Override
    public SiteResponse add(String url) throws IOException, EntityNotFoundException {
        return SiteResponse.from(siteRepository.save(Site.builder().url(url)
//                .textIds(getTextIds(url))
                .pictureIds(webScrapingService.extractPictures(url))
//                .videoIds(getVideoIds(url))
                .build()), false, false);
    }

    @Override
    public SiteResponse get(String id) throws EntityNotFoundException {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Site.class));
        return SiteResponse.from(site, isFakeSite(site), isVerified(site));
    }

    private Boolean isVerified(Site site) {
        return isAllPicturesVerified(site) && isAllVideosVerified(site) && isAllTextsVerified(site);
    }

    private Boolean isAllTextsVerified(Site site) {
        return site.getTextIds()
                .stream()
                .map(textRepository::findById)
                .flatMap(Optional::stream)
                .allMatch(Text::getVerified);
    }

    private Boolean isAllVideosVerified(Site site) {
        return site.getVideoIds()
                .stream()
                .map(videoRepository::findById)
                .flatMap(Optional::stream)
                .allMatch(Video::getVerified);
    }

    private boolean isAllPicturesVerified(Site site) {
        return site.getPictureIds()
                .stream()
                .map(pictureRepository::findById)
                .flatMap(Optional::stream)
                .allMatch(Picture::getVerified);
    }


    private Boolean isFakeSite(Site site) {
        return isFakeText(site) || isFakeVideo(site) || isFakePicture(site);
    }

    private Boolean isFakeText(Site site) {
        return site.getTextIds()
                .stream()
                .map(textRepository::findById)
                .flatMap(Optional::stream)
                .anyMatch(it -> it.getVerified() && it.getFake());
    }

    private Boolean isFakeVideo(Site site) {
        return site.getVideoIds()
                .stream()
                .map(videoRepository::findById)
                .flatMap(Optional::stream)
                .anyMatch(it -> it.getVerified() && it.getFake());
    }

    private Boolean isFakePicture(Site site) {
        return site.getPictureIds()
                .stream()
                .map(pictureRepository::findById)
                .flatMap(Optional::stream)
                .anyMatch(it -> it.getVerified() && it.getFake());
    }


}
