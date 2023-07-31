package com.cagecfi.workflow.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class User implements Serializable, UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String telephone;
    private List<String> rolesString;
    private Boolean actif;
    private Boolean pwdUpdated;
//    private Boolean isClient;
    private Boolean emailValid;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new LinkedList<>();
        rolesString.forEach(role -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
            authorities.add(grantedAuthority);
        });
        return authorities;
        /* return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));*/
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
        return pwdUpdated == null || pwdUpdated;
    }

    @Override
    public boolean isEnabled() {
        return actif == null || actif;
    }
}
