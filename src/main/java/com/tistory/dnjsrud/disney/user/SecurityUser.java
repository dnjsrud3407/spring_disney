package com.tistory.dnjsrud.disney.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@ToString
@Getter @Setter
public class SecurityUser implements UserDetails {
    private Long id;
    private String loginId;
    private String password;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : role.split(",")) {
            roles.add(new SimpleGrantedAuthority((role)));
        }
        return roles;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @QueryProjection
    public SecurityUser(Long id, String loginId, String password, String role) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

}
