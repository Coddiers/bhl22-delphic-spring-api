package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.dtos.response.VideoResponse;
import pl.edu.wat.repo.api.entities.Picture;
import pl.edu.wat.repo.api.entities.Video;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.repositories.VideoRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VideoServiceImpl implements VideoService {

    VideoRepository videoRepository;
    FileService fileService;

    @Override
    public VideoResponse add(MultipartFile file) throws IOException, EntityNotFoundException {
        FileResponse fileResponse = fileService.saveFile(file);
        return VideoResponse.from(
                videoRepository.save(
                        Video.builder()
                                .videoFileId(fileResponse.getId())
                                .build()));
    }

    @Override
    public VideoResponse getNextToVerify() throws EntityNotFoundException {
        return videoRepository.findFirstByVerifiedOrderByCreateDate(false)
                .map(VideoResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Video.class));
    }

    @Override
    public VideoResponse setAsReal(String id) throws EntityNotFoundException {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Video.class));

        video.setVerifiedDate(Instant.now());
        video.setVerified(true);
        video.setFake(false);
        return VideoResponse.from(videoRepository.save(video));
    }

    @Override
    public VideoResponse setAsFake(String id, List<MultipartFile> pictures) throws EntityNotFoundException, IOException {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Video.class));

        video.setVerifiedDate(Instant.now());
        video.setVerified(true);
        video.setFake(true);
        for (MultipartFile picture : pictures) {
            video.getResponsePictureFileIds()
                    .add(
                            fileService.saveFile(picture)
                                    .getId());
        }

        return VideoResponse.from(videoRepository.save(video));
    }

    @Override
    public VideoResponse get(String id) throws EntityNotFoundException {
        return videoRepository.findById(id)
                .map(VideoResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Video.class));
    }

    @Override
    public List<VideoResponse> getAll() {
        return videoRepository.findAll()
                .stream()
                .map(VideoResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public VideoResponse getVerified(String id) throws EntityNotFoundException {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));
        while (!video.getVerified()) {
            video = videoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Picture.class));
        }
        return VideoResponse.from(video);
    }

}
