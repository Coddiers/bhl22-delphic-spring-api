package pl.edu.wat.repo.api.dtos.request;

import lombok.Data;

@Data
public class UserRequest {
    private String surname;
    private String name;
    private String email;
    private String pesel;
    private String fatherName;
}
