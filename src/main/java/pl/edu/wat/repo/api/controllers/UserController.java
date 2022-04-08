package pl.edu.wat.repo.api.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import pl.edu.wat.repo.api.security.AuthenticationFacade;
import pl.edu.wat.repo.api.services.UserService;

@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/user")
public class UserController {

    UserService userService;
    AuthenticationFacade authenticationFacade;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.signIn(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("create_account")
    public ResponseEntity<MessageResponse> createAccount(@RequestBody CreateAccountRequest request) {
        MessageResponse messageResponse = userService.createAccount(request);
        if (messageResponse.getJwtResponse() == null) {
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(messageResponse);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("add_user")
    public ResponseEntity<MessageResponse> addNew(@RequestBody AddUserRequest userRequest) {
        return ResponseEntity.ok(userService.addNew(userRequest));
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("all")
    public ResponseEntity<List<UserResponse>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> remove(@PathVariable String id) {
        userService.removeById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.GONE);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.get(id));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserUpdateRequest request) {

        try {
            return ResponseEntity.ok(userService.update(authenticationFacade.getUserDetails().getId(), id, request));
        } catch (EntityNotFoundException | NotOwnerOfEntityException | ProhibitedOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}