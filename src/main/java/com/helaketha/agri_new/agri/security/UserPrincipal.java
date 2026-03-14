package com.helaketha.agri_new.agri.security;

import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class UserPrincipal implements Principal {

    private final String name;
    private final String userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String name, String userId, Collection<? extends GrantedAuthority> authorities) {
        this.name = Objects.requireNonNullElse(name, "");
        this.userId = userId;
        this.authorities = Collections.unmodifiableCollection(
                Objects.requireNonNullElse(authorities, Collections.emptyList()));
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}