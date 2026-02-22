# Google OAuth2 Authentication Setup

## Overview

This implementation adds Google OAuth2/OIDC social login to your existing Spring Boot e-commerce application. Users can now choose between traditional email/password authentication or Google social login, while maintaining the same JWT-based authorization system.

## Prerequisites

### 1. Google Cloud Console Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create a new project or select an existing one
3. Enable the **Google+ API** (or Google People API)
4. Go to **Credentials** section
5. Create **OAuth2 Client ID** for Web application
6. Add authorized redirect URIs:
   - `http://localhost:8386/login/oauth2/code/google` (for development)
   - `https://yourdomain.com/login/oauth2/code/google` (for production)

### 2. Update Application Configuration

Replace the placeholder values in `application.properties`:

```properties
# Replace these with your actual Google OAuth2 credentials
spring.security.oauth2.client.registration.google.client-id=YOUR_ACTUAL_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_ACTUAL_GOOGLE_CLIENT_SECRET
```

### 3. Database Setup

The application will automatically create new database columns for OAuth2 support:
- `auth_provider` (VARCHAR(20)) - Stores LOCAL or GOOGLE  
- `provider_id` (VARCHAR(100)) - Stores Google user ID
- `password` is now nullable for OAuth2 accounts

## Authentication Flows

### 1. Traditional Login (Existing)
```
POST /api/accounts/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

### 2. Google OAuth2 Login (New)
```
GET /api/accounts/auth/google
```
This redirects to Google's authorization page, then back to your success handler.

### 3. OAuth2 Success Flow
After successful Google authentication, users are redirected to:
```
http://localhost:3000/auth/success?token=JWT_TOKEN&email=user@example.com&name=User+Name
```

## API Endpoints

### OAuth2 Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/accounts/auth/google` | Initiate Google OAuth2 login |
| `GET` | `/api/accounts/auth/can-link?email={email}` | Check if email can be linked |
| `GET` | `/api/accounts/auth/provider/{email}` | Get account authentication provider |
| `POST` | `/api/accounts/auth/logout` | OAuth2 logout (JWT invalidation) |

### Example API Usage

#### Check Account Provider
```bash
GET /api/accounts/auth/provider/user@example.com

Response:
{
  "statusCode": 200,
  "message": "Provider information retrieved",
  "data": {
    "email": "user@example.com",
    "authProvider": "GOOGLE",
    "hasLocalPassword": false,
    "isOAuth2Account": true
  }
}
```

#### Check Link Capability
```bash
GET /api/accounts/auth/can-link?email=newuser@example.com

Response:
{
  "statusCode": 200,
  "message": "Email can be used for OAuth2 authentication",
  "data": {
    "email": "newuser@example.com",
    "canLink": true
  }
}
```

## Account Linking Behavior

### New Users
- Google login creates new account with **customer** role automatically
- Account uses Google email, name, and unique Google ID
- No password is required (OAuth2 authentication only)

### Existing Users
- If email matches existing account → links Google OAuth2 to existing account
- User can then login with either method (email/password OR Google)
- Account provider is updated to GOOGLE, but password remains functional

## Security Features

### JWT Integration
- Both authentication methods generate the same JWT tokens
- API authorization works identically for both login types
- Existing API endpoints require no changes

### CORS Configuration
- OAuth2 endpoints included in CORS whitelist
- Supports cross-origin requests for frontend integration

### Role-Based Access
- OAuth2 users get **customer** role by default
- Admin accounts maintain existing role structure
- `@PreAuthorize` annotations work with OAuth2 users

## Frontend Integration

### React/Vue.js Example
```javascript
// Initiate Google login
const handleGoogleLogin = () => {
  window.location.href = 'http://localhost:8386/api/accounts/auth/google';
};

// Handle successful OAuth2 callback
const handleOAuth2Success = (urlParams) => {
  const token = urlParams.get('token');
  const email = urlParams.get('email');
  const name = urlParams.get('name');
  
  // Store JWT token for API calls
  localStorage.setItem('jwt_token', token);
  localStorage.setItem('user_email', email);
  localStorage.setItem('user_name', name);
  
  // Redirect to dashboard
  router.push('/dashboard');
};

// API calls work the same way
const fetchUserData = async () => {
  const token = localStorage.getItem('jwt_token');
  const response = await fetch('/api/accounts/me', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.json();
};
```

## Testing

### Manual Testing Steps

1. **Start the application**
   ```bash
   mvn spring-boot:run
   ```

2. **Test traditional login**
   ```bash
   curl -X POST http://localhost:8386/api/accounts/login \
     -H "Content-Type: application/json" \
     -d '{"email":"existing@example.com","password":"password123"}'
   ```

3. **Test Google OAuth2 flow**
   - Open browser: `http://localhost:8386/api/accounts/auth/google`
   - Complete Google authentication
   - Verify redirect with JWT token

4. **Test API with OAuth2 token**
   ```bash
   curl -X GET http://localhost:8386/api/accounts/me \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

## Production Deployment

### Environment Variables
```bash
export GOOGLE_CLIENT_ID=your_production_client_id
export GOOGLE_CLIENT_SECRET=your_production_client_secret
export FRONTEND_URL=https://yourdomain.com
```

### Application Properties for Production
```properties
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.redirect-uri=https://yourdomain.com/login/oauth2/code/google
```

### Frontend URL Configuration
Update redirect URLs in OAuth2AuthenticationSuccessHandler:
```java
String redirectUrl = UriComponentsBuilder.fromUriString("https://yourdomain.com/auth/success")
    .queryParam("token", jwtToken)
    .build().toUriString();
```

## Troubleshooting

### Common Issues

1. **"Customer role not found"**
   - Ensure your database has a role named "customer"
   - Check role initialization in your data seeder

2. **OAuth2 redirect fails**
   - Verify Google Cloud Console redirect URIs
   - Check application.properties client credentials
   - Ensure Google+ API is enabled

3. **CORS errors in browser**
   - Verify OAuth2 endpoints in SecurityConfig permitAll()
   - Check CORS configuration allows your frontend domain

4. **JWT token invalid**
   - Ensure same JWT secret is used for both auth methods
   - Check token expiration settings

### Debug Mode
Enable OAuth2 debug logging:
```properties
logging.level.org.springframework.security.oauth2=DEBUG
logging.level.com.example.PixelMageEcomerceProject.security.oauth2=DEBUG
```

## Database Schema Changes

The implementation adds these columns to the `Accounts` table:

```sql
ALTER TABLE Accounts ADD COLUMN auth_provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL';
ALTER TABLE Accounts ADD COLUMN provider_id VARCHAR(100);
ALTER TABLE Accounts ALTER COLUMN password DROP NOT NULL;
```

## Benefits

✅ **Seamless Integration** - Works alongside existing authentication  
✅ **Account Linking** - Links Google accounts to existing emails  
✅ **JWT Consistency** - Same authorization system for all users  
✅ **Role Management** - Automatic customer role assignment  
✅ **Production Ready** - Proper error handling and security  
✅ **API Compatibility** - No changes needed to existing endpoints