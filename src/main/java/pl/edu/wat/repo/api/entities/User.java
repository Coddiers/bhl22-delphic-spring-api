package pl.edu.wat.repo.api.entities;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class User extends Entity {
    public enum ERole {
        ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN
    }

    @Indexed(unique = true)
    @NonNull
    String username;

    @Indexed(unique = true)
    @NonNull
    String email;

    @NonNull
    String password;

    @NonNull
    @Builder.Default
    Boolean isOrganization = false;

    @Builder.Default
    @NonNull
    Set<ERole> roles = new HashSet<>();

    @Nullable
    String description;
}
