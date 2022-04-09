package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface WebScrapingService {
    List<String> extractTexts(String url);

    List<String> extractPictures(String url) throws IOException;

    List<MultipartFile> extractVideos(String url);
}
