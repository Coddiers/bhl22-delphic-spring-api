package pl.edu.wat.repo.api.services;

import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WebScrapingService {
    public List<String> extractTexts(String url) {
        return Collections.emptyList(); //TODO
    }

    public List<MultipartFile> extractPictures(String url) {
        return Collections.emptyList(); //TODO
    }

    public List<MultipartFile> extractVideos(String url) {
        return Collections.emptyList(); //TODO
    }
}
