package br.com.jaya.exchangerates.converter.security;

import br.com.jaya.exchangerates.converter.entity.User;
import br.com.jaya.exchangerates.converter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    @Autowired
    private UserRepository userRepository;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        Optional<User> userOptional = userRepository.findByApikey(apiKey);

        ApiKeyAuthentication apiKeyAuthentication =null;

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            apiKeyAuthentication =  new ApiKeyAuthentication(user.getUserId(),apiKey, AuthorityUtils.NO_AUTHORITIES);
        }

        return apiKeyAuthentication;
    }
}