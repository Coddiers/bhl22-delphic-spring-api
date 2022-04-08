package pl.edu.wat.repo.api.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.util.Optional;
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

    @Override
    public FileResponse addVideo(String title, MultipartFile file) throws IOException, EntityNotFoundException {
        return addFile(title, "video", file);
    }

    @Override
    public FileResponse addFile(String title, String type, MultipartFile file) throws IOException, EntityNotFoundException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", type);
        metaData.put("title", title);
        Object fileID = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData);

        return getFile(fileRepository.save(
                File.builder()
                        .title(title)
                        .type("video")
                        .size(file.getSize())
                        .binaryId(fileID.toString())
                        .build()));

    }

    @Override
    public FileResponse getFile(String id) throws EntityNotFoundException, IOException {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(File.class));
        return getFile(file);
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
