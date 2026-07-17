package com.microservices.profile.common.util;

import com.microservices.profile.common.RoleAssigner;
import com.microservices.profile.models.enums.Roles;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultRoleAssigner implements RoleAssigner {

    @Override
    public Set<Roles> assignRoles() {
        return Set.of(Roles.USER);
    }
}
