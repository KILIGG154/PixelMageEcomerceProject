package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.RoleRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Role;
import com.example.PixelMageEcomerceProject.repository.RoleRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role createRole(RoleRequestDTO roleRequestDTO) {
        if (existsByRoleName(roleRequestDTO.getRoleName())) {
            throw new RuntimeException("Role name already exists: " + roleRequestDTO.getRoleName());
        }

        Role role = new Role();
        role.setRoleName(roleRequestDTO.getRoleName());

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(Integer roleId, RoleRequestDTO roleRequestDTO) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        // Check if role name is being changed and if new name already exists
        if (!existingRole.getRoleName().equals(roleRequestDTO.getRoleName()) &&
            existsByRoleName(roleRequestDTO.getRoleName())) {
            throw new RuntimeException("Role name already exists: " + roleRequestDTO.getRoleName());
        }

        existingRole.setRoleName(roleRequestDTO.getRoleName());

        return roleRepository.save(existingRole);
    }

    @Override
    @Transactional
    public void deleteRole(Integer roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with id: " + roleId);
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Integer roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoleName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
}

