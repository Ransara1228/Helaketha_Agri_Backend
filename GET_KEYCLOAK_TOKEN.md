# How to Get Keycloak Admin API Token for Postman

## Step 1: Get Access Token Using Client Credentials Grant

### Option A: Using Postman (Recommended)

1. **Create a new POST request** in Postman
2. **URL:** `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token`
3. **Method:** `POST`
4. **Headers:**
   - `Content-Type: application/x-www-form-urlencoded`
5. **Body (x-www-form-urlencoded):**
   - `grant_type`: `client_credentials`
   - `client_id`: `helaketha-agri-admin`
   - `client_secret`: `6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3` (from your application.yml)

6. **Send the request**

7. **Response will look like:**
   ```json
   {
     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...",
     "expires_in": 60,
     "refresh_expires_in": 1800,
     "token_type": "Bearer",
     "not-before-policy": 0,
     "scope": "profile email"
   }
   ```

8. **Copy the `access_token` value**

### Option B: Using cURL

```bash
curl -X POST "http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=helaketha-agri-admin" \
  -d "client_secret=6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3"
```

## Step 2: Use the Token in Your Admin API Request

1. **In your POST request to create user:**
   - URL: `http://localhost:8090/admin/realms/helakatha-agri-realm/users`
   - Method: `POST`
   - Authorization: Bearer Token
   - Token: Paste the `access_token` from Step 1

2. **Headers:**
   - `Content-Type: application/json`
   - `Authorization: Bearer <your-access-token>`

3. **Body (JSON):**
   ```json
    
   ```

## Step 3: Set Password After User Creation

After creating the user, you'll get a `Location` header with the user ID. Use it to set the password:

1. **URL:** `http://localhost:8090/admin/realms/helakatha-agri-realm/users/{userId}/reset-password`
   - Replace `{userId}` with the ID from the Location header
2. **Method:** `PUT`
3. **Authorization:** Bearer Token (same token)
4. **Body (JSON):**
   ```json
   {
     "type": "password",
     "value": "TempPassword123!",
     "temporary": true
   }
   ```

## Troubleshooting

### If you get 401 Unauthorized:
- Check that the `client_id` and `client_secret` are correct
- Verify the client has "Service accounts enabled" set to ON
- Make sure you're using the correct realm name: `helakatha-agri-realm`

### If you get 403 Forbidden:
- The service account needs roles assigned (see FIX_403_ERROR.md)
- Go to: Clients → helaketha-agri-admin → Service account roles
- Assign `realm-management` roles: `manage-users`, `view-users`, `query-users`

### Token Expiration:
- Tokens expire after 60 seconds (default)
- Get a new token when it expires
- You can use the refresh token to get a new access token

## Quick Test Script for Postman

You can create a Postman collection with:
1. **Request 1:** Get Token (POST to token endpoint)
2. **Request 2:** Create User (POST to users endpoint) - uses token from Request 1
3. **Request 3:** Set Password (PUT to reset-password endpoint) - uses token from Request 1

Use Postman's "Tests" tab to automatically extract and use the token:

```javascript
// In "Get Token" request, Tests tab:
var jsonData = pm.response.json();
pm.environment.set("keycloak_token", jsonData.access_token);

// In "Create User" request, Authorization tab:
// Use variable: {{keycloak_token}}
```

