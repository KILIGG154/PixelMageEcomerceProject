# Role Management API Documentation

## Overview
Complete CRUD API for Role management with Swagger documentation and Bearer Token authentication.

## Entity Structure

### Role Entity
```java
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;      // PK - Auto increment
    
    private String roleName;     // NOT NULL, Varchar(100)
    
    @OneToMany(mappedBy = "role")
    private List<Account> accounts;  // Relationship with Account
}
```

## API Endpoints

### Base URL: `/api/roles`

---

### 1. Create Role
**POST** `/api/roles`

**Request Body:**
```json
{
  "roleName": "ADMIN"
}
```

**Response:** (201 Created)
```json
{
  "code": 201,
  "message": "Role created successfully",
  "data": {
    "roleId": 1,
    "roleName": "ADMIN"
  }
}
```

**Error Response:** (400 Bad Request)
```json
{
  "code": 400,
  "message": "Failed to create role: Role name already exists: ADMIN",
  "data": null
}
```

---

### 2. Get All Roles
**GET** `/api/roles`

**Response:** (200 OK)
```json
{
  "code": 200,
  "message": "Roles retrieved successfully",
  "data": [
    {
      "roleId": 1,
      "roleName": "ADMIN"
    },
    {
      "roleId": 2,
      "roleName": "USER"
    }
  ]
}
```

---

### 3. Get Role by ID
**GET** `/api/roles/{id}`

**Path Parameter:**
- `id` (Integer) - Role ID

**Response:** (200 OK)
```json
{
  "code": 200,
  "message": "Role retrieved successfully",
  "data": {
    "roleId": 1,
    "roleName": "ADMIN"
  }
}
```

**Error Response:** (404 Not Found)
```json
{
  "code": 404,
  "message": "Role not found with id: 999",
  "data": null
}
```

---

### 4. Get Role by Name
**GET** `/api/roles/name/{roleName}`

**Path Parameter:**
- `roleName` (String) - Role name

**Response:** (200 OK)
```json
{
  "code": 200,
  "message": "Role retrieved successfully",
  "data": {
    "roleId": 1,
    "roleName": "ADMIN"
  }
}
```

**Error Response:** (404 Not Found)
```json
{
  "code": 404,
  "message": "Role not found with name: SUPERADMIN",
  "data": null
}
```

---

### 5. Update Role
**PUT** `/api/roles/{id}`

**Path Parameter:**
- `id` (Integer) - Role ID

**Request Body:**
```json
{
  "roleName": "SUPER_ADMIN"
}
```

**Response:** (200 OK)
```json
{
  "code": 200,
  "message": "Role updated successfully",
  "data": {
    "roleId": 1,
    "roleName": "SUPER_ADMIN"
  }
}
```

**Error Response:** (400 Bad Request)
```json
{
  "code": 400,
  "message": "Failed to update role: Role name already exists: USER",
  "data": null
}
```

---

### 6. Delete Role
**DELETE** `/api/roles/{id}`

**Path Parameter:**
- `id` (Integer) - Role ID

**Response:** (204 No Content)
```json
{
  "code": 204,
  "message": "Role deleted successfully",
  "data": null
}
```

**Error Response:** (404 Not Found)
```json
{
  "code": 404,
  "message": "Failed to delete role: Role not found with id: 999",
  "data": null
}
```

---

### 7. Check Role Name Exists
**GET** `/api/roles/exists/{roleName}`

**Path Parameter:**
- `roleName` (String) - Role name to check

**Response:** (200 OK)
```json
{
  "code": 200,
  "message": "Role name check completed",
  "data": true
}
```

---

## Testing with cURL

### Create Role
```bash
curl -X POST http://localhost:8386/api/roles \
  -H "Content-Type: application/json" \
  -d '{"roleName":"ADMIN"}'
```

### Get All Roles
```bash
curl http://localhost:8386/api/roles
```

### Get Role by ID
```bash
curl http://localhost:8386/api/roles/1
```

### Get Role by Name
```bash
curl http://localhost:8386/api/roles/name/ADMIN
```

### Update Role
```bash
curl -X PUT http://localhost:8386/api/roles/1 \
  -H "Content-Type: application/json" \
  -d '{"roleName":"SUPER_ADMIN"}'
```

### Delete Role
```bash
curl -X DELETE http://localhost:8386/api/roles/1
```

### Check Role Name Exists
```bash
curl http://localhost:8386/api/roles/exists/ADMIN
```

---

## Swagger UI Access

Access Swagger UI at: **http://localhost:8386/swagger-ui.html**

The API documentation includes:
- ✅ Bearer Token Authentication support
- ✅ Try-it-out functionality
- ✅ Request/Response examples
- ✅ Schema definitions

---

## Project Structure

```
src/main/java/com/example/PixelMageEcomerceProject/
├── entity/
│   └── Role.java                    # Entity with JPA annotations
├── repository/
│   └── RoleRepository.java          # JPA Repository interface
├── service/
│   ├── interfaces/
��   │   └── RoleService.java         # Service interface
│   └── impl/
│       └── RoleServiceImpl.java     # Service implementation
├── controller/
│   └── RoleController.java          # REST Controller with Swagger
└── dto/
    ├── request/
    │   └── RoleRequestDTO.java      # Request DTO
    └── response/
        ├── RoleResponseDTO.java     # Response DTO
        └── ResponseBase.java        # Standard response wrapper
```

---

## Business Logic

### Validation Rules
1. **Role name must be unique** - Cannot create/update with existing role name
2. **Role ID must exist** - Cannot update/delete non-existent role
3. **Role name is required** - Cannot create role without name

### Transaction Management
- All write operations (create, update, delete) use `@Transactional`
- All read operations use `@Transactional(readOnly = true)` for optimization

---

## Integration with Account

The Role entity has a **One-to-Many** relationship with Account:
- One Role can have multiple Accounts
- When creating an Account, you must provide a valid `roleId`
- The AccountService validates that the Role exists before creating the Account

### Example: Creating Account with Role
```json
POST /api/accounts
{
  "email": "admin@example.com",
  "password": "password123",
  "name": "Admin User",
  "phoneNumber": "0123456789",
  "roleId": 1
}
```

---

## Common Response Format

All endpoints use the `ResponseBase` format:
```java
{
  "code": <HTTP_STATUS_CODE>,
  "message": "<DESCRIPTIVE_MESSAGE>",
  "data": <RESPONSE_DATA_OR_NULL>
}
```

---

## Security Configuration

- Role endpoints are currently in `permitAll()` for development
- For production, remove `/api/roles/**` from SecurityConfig permitAll list
- Add Bearer token authentication requirement
- Consider role-based access control (only ADMIN can manage roles)

---

## Next Steps (Recommendations)

1. ✅ Add validation annotations (@NotBlank, @Size)
2. ✅ Create Global Exception Handler
3. ✅ Add pagination for getAllRoles
4. ✅ Implement role-based authorization
5. ✅ Add audit fields (createdAt, updatedAt, createdBy, updatedBy)
6. ✅ Write unit tests and integration tests
7. ✅ Add role hierarchy support
8. ✅ Implement role permission mapping

