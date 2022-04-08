package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.dtos.response.VideoResponse;
import pl.edu.wat.repo.api.entities.Video;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.repositories.VideoRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VideoServiceImpl implements VideoService {

    GridFsTemplate gridFsTemplate;
    GridFsOperations operations;
    VideoRepository videoRepository;
    FileService fileService;

    @Override
    public VideoResponse add(String title, MultipartFile file) throws IOException, EntityNotFoundException {
        FileResponse fileResponse = fileService.addVideo(title, file);
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
            video.getPicturesIds()
                    .add(
                            fileService.addPicture(video.getId(), picture)
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


}
