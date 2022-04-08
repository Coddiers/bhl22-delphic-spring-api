package pl.edu.wat.repo.api.dtos.request;

import java.util.List;
import javax.validation.constraints.Null;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {
    @Null
    List<MultipartFile> photos;

    @Null
    String description;

    @Null
    Boolean isOrganization;
}
