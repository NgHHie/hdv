package com.example.security_service.services;

import com.example.security_service.models.Permission;
import com.example.security_service.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Transactional
    public Permission createPermission(Permission permission) {
        // Check if permission with the same name already exists
        if (permissionRepository.findByName(permission.getName()).isPresent()) {
            throw new RuntimeException("Permission with name " + permission.getName() + " already exists");
        }
        
        return permissionRepository.save(permission);
    }

    @Transactional
    public Permission updatePermission(Long id, Permission permissionDetails) {
        Permission permission = getPermissionById(id);
        
        // Check if we're trying to change the name and if that name is already taken
        if (!permission.getName().equals(permissionDetails.getName()) && 
                permissionRepository.findByName(permissionDetails.getName()).isPresent()) {
            throw new RuntimeException("Permission name already exists: " + permissionDetails.getName());
        }
        
        permission.setName(permissionDetails.getName());
        permission.setPath(permissionDetails.getPath());
        permission.setMethod(permissionDetails.getMethod());
        permission.setDescription(permissionDetails.getDescription());
        
        return permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = getPermissionById(id);
        permissionRepository.delete(permission);
    }

    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }
}
