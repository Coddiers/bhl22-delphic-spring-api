package pl.edu.wat.repo.api.security;

import org.springframework.security.core.Authentication;
import pl.edu.wat.repo.api.services.UserDetailsImpl;

public interface AuthenticationFacade {
    Authentication getAuthentication();

    UserDetailsImpl getUserDetails();
}
