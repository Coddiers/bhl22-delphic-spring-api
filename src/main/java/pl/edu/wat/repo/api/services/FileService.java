package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface FileService {
    FileResponse saveFile(MultipartFile file) throws IOException, EntityNotFoundException;

    FileResponse getFile(String id) throws IllegalStateException, IOException, EntityNotFoundException;

    FileResponse saveFile(InputStream inputStream, String originalFileName, String contentType,
                          Long size) throws IOException, EntityNotFoundException;
}
