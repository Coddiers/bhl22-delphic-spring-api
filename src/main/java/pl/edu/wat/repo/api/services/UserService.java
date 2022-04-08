package pl.edu.wat.repo.api.services;

import java.util.List;
import pl.edu.wat.repo.api.dtos.request.AddUserRequest;
import pl.edu.wat.repo.api.dtos.request.CreateAccountRequest;
import pl.edu.wat.repo.api.dtos.request.LoginRequest;
import pl.edu.wat.repo.api.dtos.request.UserUpdateRequest;
import pl.edu.wat.repo.api.dtos.response.JwtResponse;
import pl.edu.wat.repo.api.dtos.response.MessageResponse;
import pl.edu.wat.repo.api.dtos.response.UserResponse;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.exceptions.NotOwnerOfEntityException;
import pl.edu.wat.repo.api.exceptions.ProhibitedOperationException;

public interface UserService {
    List<UserResponse> getAll();

    MessageResponse createAccount(CreateAccountRequest request);

    MessageResponse addNew(AddUserRequest userRequest);

    void removeById(String userId);

    UserResponse get(String userId) throws EntityNotFoundException;

    JwtResponse signIn(LoginRequest loginRequest);

    UserResponse update(String executorUserId, String userId, UserUpdateRequest request) throws EntityNotFoundException, NotOwnerOfEntityException,
            ProhibitedOperationException;
}
