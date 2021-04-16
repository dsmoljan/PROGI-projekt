package hr.fer.progi.dogGO.service.impl;

import hr.fer.progi.dogGO.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceJpa implements AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${doggo.admin.password}")
    private String adminPasswordHash;

    @Override
    public boolean login(String username, String password) {
        return "admin".equals(username) && passwordEncoder.matches(password, adminPasswordHash);
    }
}
