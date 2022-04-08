package pl.edu.wat.repo.api.dtos.request;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String username;
    private String email;
    private Set<String> role;
    private String password;
    private String surname;
    private String name;
    private String pesel;
    private String fatherName;

}
