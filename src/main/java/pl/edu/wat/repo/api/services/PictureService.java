package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.PictureResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface PictureService {
    PictureResponse add(InputStream inputStream, String name, long size) throws IOException, EntityNotFoundException;

    PictureResponse add(MultipartFile file) throws IOException, EntityNotFoundException;

    PictureResponse getNextToVerify() throws EntityNotFoundException;

    PictureResponse setAsReal(String id) throws EntityNotFoundException;

    PictureResponse setAsFake(String id, List<MultipartFile> pictures) throws EntityNotFoundException, IOException;

    PictureResponse get(String id) throws EntityNotFoundException;
}
