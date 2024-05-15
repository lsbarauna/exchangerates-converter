package br.com.jaya.exchangerates.converter.security;

import br.com.jaya.exchangerates.converter.to.ErrorOutbound;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationFilter extends GenericFilterBean {

    private AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

            Authentication authentication = authenticationService.getAuthentication((HttpServletRequest) request);

        if(authentication!=null
                    || ((HttpServletRequest) request).getServletPath().equals("/exchangerates/users")
                || ((HttpServletRequest) request).getServletPath().equals("/exchangerates/new-apikey")
                || ((HttpServletRequest) request).getServletPath().contains("swagger")
                || ((HttpServletRequest) request).getServletPath().contains("api-docs")
            ){
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }else {
                sendNotAuthorizedRespose((HttpServletResponse) response);
            }

    }

    private static void sendNotAuthorizedRespose(HttpServletResponse response) throws IOException {
        HttpServletResponse httpResponse = response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = httpResponse.getWriter();
        ErrorOutbound errorResponse = new ErrorOutbound(HttpStatus.UNAUTHORIZED, "You have not provided a valid API Access Key. [Required format: X-API-KEY=YOUR_ACCESS_KEY]");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);
        writer.print(json);
        writer.flush();
        writer.close();
    }
}