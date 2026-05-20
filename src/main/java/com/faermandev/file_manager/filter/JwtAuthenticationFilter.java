package com.faermandev.file_manager.filter;

import com.faermandev.file_manager.repository.UserRepository;
import com.faermandev.file_manager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // vai ler o header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // vai pegar apenas o token sem o Bearer e extrair o email desse token
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        // verifica se o email não é null e o usuário ainda não foi autenticado
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var user = userRepository.findByEmail(email);

            // usa user.isPresent() pois findByEmail vai retornar um Optional<User>
            if (user.isPresent() && jwtService.isTokenValid(token, email)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        user.get(),
                        null,
                        List.of()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}