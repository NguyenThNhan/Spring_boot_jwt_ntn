package com.example.spring_boot_jwt_ntn.filter;

import com.example.spring_boot_jwt_ntn.authen.UserPrincipal;
import com.example.spring_boot_jwt_ntn.entities.Token;
import com.example.spring_boot_jwt_ntn.service.TokenService;
import com.example.spring_boot_jwt_ntn.util.JwtUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService verificationTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader
                = request.getHeader("Authorization");

        UserPrincipal user = null;
        Token token = null;


        if (StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith("Token ")) {
            String jwt = authorizationHeader.substring(6);

            user = jwtUtil.getUserFromToken(jwt);
            token = verificationTokenService.findByToken(jwt);
        }

        if (null != user && null != token && token.getTokenExpDate().after(new Date())) {

            Set<GrantedAuthority> authorities = new HashSet<>();

            user.getAuthorities().forEach(
                    p -> authorities.add(new SimpleGrantedAuthority((String) p)));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    @Data
    @Getter
    @Setter
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    public abstract static class BaseEntity implements Serializable {
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String deleted;

        @CreatedDate
        private Date createdAt;

        @LastModifiedDate
        private Date updatedAt;

        private Long createdBy;

        private Long updatedBy;
    }
}
