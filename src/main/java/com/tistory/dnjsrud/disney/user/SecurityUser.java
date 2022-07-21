package com.tistory.dnjsrud.disney.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@ToString
@Getter @Setter
public class SecurityUser implements UserDetails, OAuth2User {
    // UserDetails
    private Long id;
    private String loginId;
    private String password;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;

    // OAuth2User
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

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

    @Override
    public String getName() {
        return attributes.get("sub").toString();
    }

    /**
     * 일반 로그인 시 사용
     * @param id
     * @param loginId
     * @param password
     * @param role
     */
    @QueryProjection
    public SecurityUser(Long id, String loginId, String password, String role) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

    /**
     * OAuth 로그인 시 사용
     * @param user
     * @param attributes
     */
    public SecurityUser(User user, Map<String, Object> attributes) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.attributes = attributes;
    }
}
