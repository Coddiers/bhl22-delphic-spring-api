package pl.edu.wat.repo.api.dtos.response;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.repo.api.entities.File;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileResponse {

    String id;
    String title;
    Long size;
    String type;
    byte[] file;

    public static FileResponse from(File file, byte[] data) {
        return new FileResponse(
                file.getId(),
                file.getTitle(),
                file.getSize(),
                file.getType(),
                data
        );
    }
}
