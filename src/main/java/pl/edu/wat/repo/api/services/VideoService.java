package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.VideoResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;

public interface VideoService {
    VideoResponse add(MultipartFile file) throws IOException, EntityNotFoundException;

    VideoResponse getNextToVerify() throws EntityNotFoundException;

    VideoResponse setAsReal(String id) throws EntityNotFoundException;

    VideoResponse setAsFake(String id, List<MultipartFile> pictures) throws EntityNotFoundException, IOException;

    VideoResponse get(String id) throws EntityNotFoundException;

    List<VideoResponse> getAll();

    VideoResponse getVerified(String id) throws EntityNotFoundException;
}
