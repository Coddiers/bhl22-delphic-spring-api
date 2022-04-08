package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.SiteResponse;
import pl.edu.wat.repo.api.entities.Site;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SiteServiceImpl implements SiteService {

    VideoService videoService;
    PictureService pictureService;
    TextService textService;
    WebScrapingService webScrapingService;

    @Override
    public SiteResponse add(String url) throws IOException, EntityNotFoundException {
        return SiteResponse.from(Site.builder()
                .url(url)
                .textIds(getTextIds(url))
                .pictureIds(getPictureIds(url))
                .videoIds(getVideoIds(url))
                .build());
    }

    private List<String> getVideoIds(String url) throws IOException, EntityNotFoundException {
        List<MultipartFile> files = webScrapingService.extractVideos(url);
        List<String> ids = new ArrayList<>();

        for (MultipartFile file : files) {
            ids.add(videoService.add(file).getId());
        }
        return ids;
    }

    private List<String> getPictureIds(String url) throws IOException, EntityNotFoundException {
        List<MultipartFile> files = webScrapingService.extractPictures(url);
        List<String> ids = new ArrayList<>();

        for (MultipartFile file : files) {
            ids.add(pictureService.add(file).getId());
        }
        return ids;
    }

    private List<String> getTextIds(String url) {
        return webScrapingService.extractTexts(url)
                .stream()
                .map(text -> textService.add(text).getId())
                .collect(Collectors.toList());
    }
}
