package br.com.jaya.exchangerates.converter.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String apiKey;
    private final Long userId;

    public ApiKeyAuthentication(Long  userId,String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    public Long getUserId() {
        return userId;
    }
}