package hr.fer.progi.dogGO.rest.security;

import hr.fer.progi.dogGO.domain.WalkerLogin;
import hr.fer.progi.dogGO.repository.WalkerLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("walkerUserDetailsService")
public class WalkerUserDetailsService implements UserDetailsService {
    @Autowired
    private WalkerLoginRepository walkerLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<WalkerLogin> optionalWalkerLogin = walkerLoginRepository.findByUsername(username);
        if (optionalWalkerLogin.isPresent()) {
            System.out.println("yo");
            WalkerLogin walkerLogin = optionalWalkerLogin.get();
            return new User(username, walkerLogin.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_WALKER"));
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }
}
