package com.example.security_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.security_service.models.ERole;
import com.example.security_service.models.Permission;
import com.example.security_service.models.Role;
import com.example.security_service.repositories.PermissionRepository;
import com.example.security_service.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    public Role getRoleByName(String roleName) {
        try {
            ERole eRole = ERole.valueOf("ROLE_" + roleName.toUpperCase());
            return roleRepository.findByName(eRole)
                    .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }
    }

    @Transactional
    public Role updateRole(Long id, Role roleDetails) {
        Role role = getRoleById(id);
        
        // We don't allow changing the role name
        role.setDescription(roleDetails.getDescription());
        
        return roleRepository.save(role);
    }

    @Transactional
    public Role addPermissionToRole(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
        
        // Check if permission is already associated with role
        if (role.getPermissions().contains(permission)) {
            throw new RuntimeException("Permission is already assigned to role");
        }
        
        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }

    @Transactional
    public Role removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
        
        // Check if permission is associated with role
        if (!role.getPermissions().contains(permission)) {
            throw new RuntimeException("Permission is not assigned to role");
        }
        
        role.getPermissions().remove(permission);
        return roleRepository.save(role);
    }
}