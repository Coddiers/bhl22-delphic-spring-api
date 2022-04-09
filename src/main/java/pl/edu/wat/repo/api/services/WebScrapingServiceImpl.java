package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import pl.edu.wat.repo.api.dtos.response.PictureResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WebScrapingServiceImpl implements WebScrapingService {

    PictureService pictureService;

    @Override
    public List<String> extractTexts(String url) {
        return Collections.emptyList(); //TODO
    }

    @Override
    public List<String> extractPictures(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla")
                .get()
                .body()
                .getElementsByTag("img")
                .stream()
                .map(it -> it.attr("src"))
                .filter(StringUtils::isNotBlank)
                .map(this::fixPathIfNoProtocol)
                .distinct()
                .map(this::downloadPicture)
                .flatMap(Optional::stream)
                .map(PictureResponse::getId)
                .toList();
    }

    private String fixPathIfNoProtocol(String path) {
        return path.replaceAll("^//","https://");
    }

    private Optional<PictureResponse> downloadPicture(String url) {
        try (InputStream input = new URL(url).openStream()) {
            return Optional.of(pictureService.add(input, url,99L));
        } catch (IOException | EntityNotFoundException e) {
            log.error("Error ",e);
            return Optional.empty();
        }
    }

    public List<MultipartFile> extractVideos(String url) {
        return Collections.emptyList(); //TODO
    }
}
