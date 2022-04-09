package pl.edu.wat.repo.api.dtos.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.edu.wat.repo.api.entities.User;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String description;
    Boolean isOrganization;
    List<String> roles;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDescription(),
                user.getIsOrganization(),
                user.getRoles().stream().map(Enum::name).toList()
        );
    }
}
