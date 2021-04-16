package hr.fer.progi.dogGO.rest.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("adminUserDetailsService")
public class AdminUserDetailsService implements UserDetailsService {
    @Value("${doggo.admin.password}")
    private String adminPasswordHash;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) { 
            return new User(username, adminPasswordHash,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }
}
