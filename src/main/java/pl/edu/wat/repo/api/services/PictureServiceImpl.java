package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.dtos.response.PictureResponse;
import pl.edu.wat.repo.api.entities.Picture;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.repositories.PictureRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PictureServiceImpl implements PictureService {

    PictureRepository pictureRepository;
    FileService fileService;

    @Override
    public PictureResponse add(MultipartFile file) throws IOException, EntityNotFoundException {
        FileResponse fileResponse = fileService.savePicture(file);
        return PictureResponse.from(
                pictureRepository.save(
                        Picture.builder()
                                .pictureFileId(fileResponse.getId())
                                .build()));
    }

    @Override
    public PictureResponse getNextToVerify() throws EntityNotFoundException {
        return pictureRepository.findFirstByVerifiedOrderByCreateDate(false)
                .map(PictureResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));
    }

    @Override
    public PictureResponse setAsReal(String id) throws EntityNotFoundException {
        Picture video = pictureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));

        video.setVerifiedDate(Instant.now());
        video.setVerified(true);
        video.setFake(false);
        return PictureResponse.from(pictureRepository.save(video));
    }

    @Override
    public PictureResponse setAsFake(String id, List<MultipartFile> pictures) throws EntityNotFoundException, IOException {
        Picture video = pictureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));

        video.setVerifiedDate(Instant.now());
        video.setVerified(true);
        video.setFake(true);
        for (MultipartFile picture : pictures) {
            video.getResponsePictureFileIds()
                    .add(
                            fileService.savePicture(picture)
                                    .getId());
        }

        return PictureResponse.from(pictureRepository.save(video));
    }

    @Override
    public PictureResponse get(String id) throws EntityNotFoundException {
        return pictureRepository.findById(id)
                .map(PictureResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));
    }


}
