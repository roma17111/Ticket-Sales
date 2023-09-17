package ru.service.ticketsales.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {

    BUYER("BUYER"),
    ADMIN("ADMIN")
    ;

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
