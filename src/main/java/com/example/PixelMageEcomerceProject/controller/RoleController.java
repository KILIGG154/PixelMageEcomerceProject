package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.RoleRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Role;
import com.example.PixelMageEcomerceProject.service.interfaces.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "APIs for managing roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;

    /**
     * Create a new role
     */
    @PostMapping
    @Operation(
            summary = "Create a new role",
            description = "Create a new role with role name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Role name already exists",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createRole(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Role details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoleRequestDTO.class))
            )
            @RequestBody RoleRequestDTO roleRequestDTO) {
        try {
            Role createdRole = roleService.createRole(roleRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Role created successfully",
                    createdRole
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create role: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Get all roles
     */
    @GetMapping
    @Operation(
            summary = "Get all roles",
            description = "Retrieve a list of all roles in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Roles retrieved successfully",
                roles
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get role by ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get role by ID",
            description = "Retrieve role details by role ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getRoleById(
            @Parameter(description = "Role ID", required = true)
            @PathVariable Integer id) {
        return roleService.getRoleById(id)
                .map(role -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Role retrieved successfully",
                            role
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Role not found with id: " + id,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /**
     * Get role by name
     */
    @GetMapping("/name/{roleName}")
    @Operation(
            summary = "Get role by name",
            description = "Retrieve role details by role name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getRoleByName(
            @Parameter(description = "Role name", required = true)
            @PathVariable String roleName) {
        return roleService.getRoleByName(roleName)
                .map(role -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Role retrieved successfully",
                            role
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Role not found with name: " + roleName,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /**
     * Update role
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update role",
            description = "Update existing role information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or role name already exists",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateRole(
            @Parameter(description = "Role ID", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated role details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RoleRequestDTO.class))
            )
            @RequestBody RoleRequestDTO roleRequestDTO) {
        try {
            Role updatedRole = roleService.updateRole(id, roleRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Role updated successfully",
                    updatedRole
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to update role: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Delete role
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete role",
            description = "Delete a role by role ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteRole(
            @Parameter(description = "Role ID", required = true)
            @PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.NO_CONTENT.value(),
                    "Role deleted successfully",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to delete role: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Check if role name exists
     */
    @GetMapping("/exists/{roleName}")
    @Operation(
            summary = "Check if role name exists",
            description = "Check if a role name is already registered in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role name check completed",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> checkRoleNameExists(
            @Parameter(description = "Role name to check", required = true)
            @PathVariable String roleName) {
        boolean exists = roleService.existsByRoleName(roleName);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Role name check completed",
                exists
        );
        return ResponseEntity.ok(response);
    }
}

