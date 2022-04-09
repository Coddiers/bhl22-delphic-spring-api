package pl.edu.wat.repo.api.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public PictureResponse add(InputStream inputStream, String name, long size) throws IOException, EntityNotFoundException {
        FileResponse fileResponse = fileService.saveFile(inputStream, name, MediaType.IMAGE_JPEG_VALUE, size);
        return PictureResponse.from(
                pictureRepository.save(
                        Picture.builder()
                                .pictureFileId(fileResponse.getId())
                                .build()));
    }

    @Override
    public PictureResponse add(MultipartFile file) throws IOException, EntityNotFoundException {
        FileResponse fileResponse = fileService.saveFile(file);
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
        Picture pictureEntity = pictureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));

        pictureEntity.setVerifiedDate(Instant.now());
        pictureEntity.setVerified(true);
        pictureEntity.setFake(true);
        for (MultipartFile picture : pictures) {
            pictureEntity.getResponsePictureFileIds()
                    .add(
                            fileService.saveFile(picture)
                                    .getId());
        }

        return PictureResponse.from(pictureRepository.save(pictureEntity));
    }

    @Override
    public PictureResponse get(String id) throws EntityNotFoundException {
        return pictureRepository.findById(id)
                .map(PictureResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));
    }

    @Override
    public List<PictureResponse> getAll() {
        return pictureRepository.findAll()
                .stream()
                .map(PictureResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public PictureResponse getVerified(String id) throws EntityNotFoundException {
        Picture picture = pictureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Picture.class));
        while (!picture.getVerified()) {
            picture = pictureRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Picture.class));
        }
        return PictureResponse.from(picture);
    }
}
