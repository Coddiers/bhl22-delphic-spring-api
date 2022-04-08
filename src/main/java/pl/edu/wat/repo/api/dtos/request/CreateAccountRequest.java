package pl.edu.wat.repo.api.dtos.request;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String username;
    private String email;
    private String password;
}
