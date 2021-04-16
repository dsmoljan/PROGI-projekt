package hr.fer.progi.dogGO.rest.security;

import hr.fer.progi.dogGO.domain.AssociationLogin;
import hr.fer.progi.dogGO.repository.AssociationLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("associationUserDetailsService")
public class AssociationUserDetailsService implements UserDetailsService {

    @Autowired
    private AssociationLoginRepository associationLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AssociationLogin> optionalAssociationLogin = associationLoginRepository.findByUsername(username);
        if (optionalAssociationLogin.isPresent()) {
            AssociationLogin associationLogin = optionalAssociationLogin.get();
            return new User(username, associationLogin.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ASSOCIATION"));
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }
}
