package pl.edu.wat.repo.api.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface UserDetailsService {
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
