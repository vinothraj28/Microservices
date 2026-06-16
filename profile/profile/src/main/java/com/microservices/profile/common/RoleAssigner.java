package com.microservices.profile.common;

import com.microservices.profile.models.enums.Roles;

import java.util.Set;

public interface RoleAssigner {
    Set<Roles> assignRoles();
}
