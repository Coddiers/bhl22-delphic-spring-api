package pl.edu.wat.repo.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.wat.repo.api.services.UserDetailsImpl;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserDetailsImpl getUserDetails() {
        return (UserDetailsImpl) this.getAuthentication().getPrincipal();
    }
}