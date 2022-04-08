package pl.edu.wat.repo.api.services;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.repo.api.dtos.request.AddUserRequest;
import pl.edu.wat.repo.api.dtos.request.CreateAccountRequest;
import pl.edu.wat.repo.api.dtos.request.LoginRequest;
import pl.edu.wat.repo.api.dtos.request.UserUpdateRequest;
import pl.edu.wat.repo.api.dtos.response.JwtResponse;
import pl.edu.wat.repo.api.dtos.response.MessageResponse;
import pl.edu.wat.repo.api.dtos.response.UserResponse;
import pl.edu.wat.repo.api.entities.User;
import pl.edu.wat.repo.api.exceptions.EntityNotFoundException;
import pl.edu.wat.repo.api.exceptions.NotOwnerOfEntityException;
import pl.edu.wat.repo.api.exceptions.ProhibitedOperationException;
import pl.edu.wat.repo.api.repositories.UserRepository;
import pl.edu.wat.repo.api.security.jwt.JwtUtils;
import pl.edu.wat.repo.api.utils.UserUtils;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder encoder;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    @Override
    public MessageResponse createAccount(CreateAccountRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername()))) {
            return MessageResponse.builder()
                    .message("Error: Username is already taken!")
                    .build();
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            return MessageResponse.builder()
                    .message("Error: Email is already in use!")
                    .build();
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(User.ERole.ROLE_USER))
                .build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        return MessageResponse.builder()
                .message("User registered successfully!")
                .jwtResponse(signIn(loginRequest))
                .build();
    }

    @Override
    public MessageResponse addNew(@RequestBody AddUserRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername()))) {
            return MessageResponse.builder()
                    .message("Error: Username is already taken!")
                    .build();
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            return MessageResponse.builder()
                    .message("Error: Email is already in use!")
                    .build();
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .roles(getRolesWithNames(request.getRole()))
                .build();
        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        return MessageResponse.builder()
                .message("User added successfully!")
                .jwtResponse(signIn(loginRequest))
                .build();
    }

    private Set<User.ERole> getRolesWithNames(Set<String> roles) {
        return roles.stream()
                .filter(StringUtils::isNotBlank)
                .map(name ->
                        switch (name) {
                            case "admin" -> User.ERole.ROLE_ADMIN;
                            case "user" -> User.ERole.ROLE_USER;
                            case "mod" -> User.ERole.ROLE_MODERATOR;
                            default -> null;
                        })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeById(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse get(String userId) throws EntityNotFoundException {
        return UserResponse.from(
                userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException(User.class)));
    }

    @Override
    public UserResponse update(String executorId, String userId, UserUpdateRequest request) throws EntityNotFoundException, NotOwnerOfEntityException,
            ProhibitedOperationException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        User executorUser = userRepository.findById(executorId)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        if (!executorUser.equals(user) && UserUtils.isNotModOrAdmin(executorUser)) {
            throw new NotOwnerOfEntityException();
        }

        if (request.getIsOrganization() != null) {
            if (UserUtils.isNotModOrAdmin(executorUser)) {
                throw new ProhibitedOperationException();
            }
            user.setIsOrganization(request.getIsOrganization());
        }

        if (request.getDescription() != null) {
            user.setDescription(request.getDescription());
        }

        return UserResponse
                .from(userRepository.save(user));

    }

}
