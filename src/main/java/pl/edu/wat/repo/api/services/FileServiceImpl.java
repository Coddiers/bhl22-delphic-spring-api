package pl.edu.wat.repo.api.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.repo.api.dtos.response.FileResponse;
import pl.edu.wat.repo.api.entities.File;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.repositories.FileRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileServiceImpl implements FileService {

    GridFsTemplate gridFsTemplate;
    GridFsOperations operations;
    FileRepository fileRepository;

    private static String genTitle(String id) {
        return "%s_%s".formatted(id, UUID.randomUUID().toString());
    }

    @Override
    public FileResponse saveFile(MultipartFile file) throws IOException, EntityNotFoundException {
        return saveFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), file.getSize());
    }

    @Override
    public FileResponse getFile(String id) throws EntityNotFoundException, IOException {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(File.class));
        return getFile(file);
    }

    public FileResponse savePicture(MultipartFile file) throws IOException, EntityNotFoundException {
        return saveFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), file.getSize());
    }

    @Override
    public FileResponse saveFile(InputStream inputStream, String originalFileName, String contentType, Long size)
            throws IOException, EntityNotFoundException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", contentType);
        metaData.put("title", originalFileName);
        Object fileID = gridFsTemplate.store(inputStream, originalFileName, contentType, metaData);

        return getFile(fileRepository.save(
                File.builder()
                        .title(originalFileName)
                        .type(contentType)
                        .size(size)
                        .binaryId(fileID.toString())
                        .build()));

    }

    private FileResponse getFile(File file) throws EntityNotFoundException, IOException {
        GridFSFile gridFSFile = Optional.ofNullable(
                        gridFsTemplate.findOne(new Query(Criteria.where("_id").is(file.getBinaryId()))))
                .orElseThrow(() -> new EntityNotFoundException(GridFSFile.class));
        return FileResponse.from(
                file,
                IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
    }
}
