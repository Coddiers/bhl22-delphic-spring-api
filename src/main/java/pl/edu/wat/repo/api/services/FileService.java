package pl.edu.wat.repo.api.services;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface FileService {
    FileResponse saveVideo(MultipartFile file) throws IOException, EntityNotFoundException;

    FileResponse getFile(String id) throws IllegalStateException, IOException, EntityNotFoundException;

    FileResponse savePicture(MultipartFile pictureFile) throws IOException, EntityNotFoundException;
}
