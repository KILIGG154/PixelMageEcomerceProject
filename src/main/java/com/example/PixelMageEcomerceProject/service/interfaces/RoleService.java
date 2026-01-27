package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.RoleRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    /**
     * Create a new role
     */
    Role createRole(RoleRequestDTO roleRequestDTO);

    /**
     * Update an existing role
     */
    Role updateRole(Integer roleId, RoleRequestDTO roleRequestDTO);

    /**
     * Delete a role by ID
     */
    void deleteRole(Integer roleId);

    /**
     * Get role by ID
     */
    Optional<Role> getRoleById(Integer roleId);

    /**
     * Get role by name
     */
    Optional<Role> getRoleByName(String roleName);

    /**
     * Get all roles
     */
    List<Role> getAllRoles();

    /**
     * Check if role name exists
     */
    boolean existsByRoleName(String roleName);
}

