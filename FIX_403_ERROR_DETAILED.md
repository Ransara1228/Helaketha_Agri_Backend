# Fix 403 Forbidden Error - Detailed Steps

## The Problem
You're seeing realm roles assigned (like `manage-users`, `view-users`), but the service account needs **CLIENT ROLES** from the `realm-management` client, not realm roles.

## Solution: Assign Client Roles from realm-management

### Step 1: Navigate to Service Account Roles
1. Go to Keycloak Admin Console: `http://localhost:8090`
2. Select realm: `helakatha-agri-realm`
3. Go to **Clients** → `helaketha-agri-admin`
4. Click on **Service account roles** tab

### Step 2: Assign Client Roles (IMPORTANT!)
You're currently seeing **Realm roles**. You need to assign **Client roles** from `realm-management`:

1. **Click the "Assign role" button** (it has a dropdown arrow)
2. **Select "Filter by clients"** or look for a dropdown that says "Filter by clients"
3. **Select `realm-management`** from the client filter dropdown
4. You should now see client roles like:
   - `manage-users` (client role)
   - `view-users` (client role)
   - `query-users` (client role)
   - `manage-realm` (client role)
5. **Check the boxes** for these roles:
   - ✅ `manage-users` (REQUIRED)
   - ✅ `view-users` (optional, for searching)
   - ✅ `query-users` (optional, for querying)
   - ✅ `manage-realm` (optional, for full realm management)
6. **Click "Assign"** button

### Step 3: Verify Client Roles Are Assigned
After assigning, you should see TWO sections in the roles table:
1. **Realm roles** section (what you currently see)
2. **Client roles** section with `realm-management` as the client

The client roles should show:
- `realm-management` → `manage-users`
- `realm-management` → `view-users`
- `realm-management` → `query-users`
- `realm-management` → `manage-realm`

### Step 4: Alternative Method - Via Service Account User
If the above doesn't work, try this method:

1. Go to **Users** in the left sidebar
2. Click on **Service accounts** tab (or filter by "Service accounts")
3. Find and click on: `service-account-helaketha-agri-admin`
4. Go to **Role mapping** tab
5. Click **Assign role** button
6. **Filter by clients** → Select `realm-management`
7. Select the roles: `manage-users`, `view-users`, `query-users`
8. Click **Assign**

### Step 5: Test the API
1. Get a new access token (tokens expire after 60 seconds)
2. Use the token in your Postman request
3. You should now get `201 Created` instead of `403 Forbidden`

## Key Difference

- ❌ **Realm roles** (what you currently have): These are roles at the realm level, not sufficient for Admin API
- ✅ **Client roles from realm-management**: These are the roles that grant Admin API permissions

## Visual Guide

When you click "Assign role" → "Filter by clients" → "realm-management", you should see:

```
Available roles (from realm-management client):
☐ manage-users
☐ view-users  
☐ query-users
☐ manage-realm
☐ view-realm
☐ ... (other realm-management roles)
```

Select the ones you need and click "Assign".

## Still Getting 403?

1. **Verify the token is valid** - Get a fresh token
2. **Check token expiration** - Tokens expire in 60 seconds
3. **Verify client secret** - Make sure `6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3` matches what's in Keycloak
4. **Restart Keycloak** - Sometimes role changes need a restart
5. **Check the token scope** - The token should include the roles in its claims

## Quick Test

After assigning roles, test with this curl command:

```bash
# 1. Get token
TOKEN=$(curl -X POST "http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=helaketha-agri-admin" \
  -d "client_secret=6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3" | jq -r '.access_token')

# 2. Create user
curl -X POST "http://localhost:8090/admin/realms/helakatha-agri-realm/users" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "enabled": true
  }'
```

If you get `201 Created`, the permissions are correct!

