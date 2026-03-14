# Fix 403 Error - Assign Client Roles to Service Account

## You're on the Right Page! ✅

You're currently at: `http://localhost:8090/admin/master/console/#/helakatha-agri-realm/clients/{client-id}/roles`

This is the **client roles** page for `helaketha-agri-admin`. However, you need to assign **client roles FROM `realm-management`** to the **service account**.

## Step-by-Step Instructions

### Method 1: Via Service Account Roles Tab (EASIEST)

1. **Go back to the client settings:**
   - Click on **"Clients"** in the left sidebar
   - Click on **`helaketha-agri-admin`** client
   - Click on the **"Service account roles"** tab (NOT "Roles" tab)

2. **Assign Client Roles:**
   - Click the **"Assign role"** button (it has a dropdown arrow)
   - Look for a dropdown that says **"Filter by clients"** or **"Filter clients"**
   - Select **`realm-management`** from the filter dropdown
   - You should now see client roles from `realm-management`:
     - ☐ `manage-users`
     - ☐ `view-users`
     - ☐ `query-users`
     - ☐ `manage-realm`
     - ... (other roles)
   
3. **Select the required roles:**
   - ✅ Check **`manage-users`** (REQUIRED - for creating/deleting users)
   - ✅ Check **`view-users`** (optional - for searching users)
   - ✅ Check **`query-users`** (optional - for querying users)
   - ✅ Check **`manage-realm`** (optional - for full realm management)

4. **Click "Assign"** button

5. **Verify:**
   - You should see a new section in the roles table showing:
     ```
     Client roles:
     realm-management → manage-users
     realm-management → view-users
     realm-management → query-users
     realm-management → manage-realm
     ```

### Method 2: Via Service Account User (Alternative)

1. **Go to Users:**
   - Click **"Users"** in the left sidebar
   - Look for a **"Service accounts"** tab or filter
   - Search for: `service-account-helaketha-agri-admin`
   - Click on that user

2. **Go to Role Mapping:**
   - Click on **"Role mapping"** tab
   - Click **"Assign role"** button
   - Select **"Filter by clients"**
   - Select **`realm-management`**
   - Check the roles: `manage-users`, `view-users`, `query-users`
   - Click **"Assign"**

## Important: Client Roles vs Realm Roles

- ❌ **Realm roles** (what you might see in the main table) - NOT sufficient
- ✅ **Client roles from `realm-management`** - These are what you need!

The roles must come from the **`realm-management` CLIENT**, not from the realm itself.

## After Assigning Roles

1. **Restart your Spring Boot application**
2. **Test creating a farmer** - The 403 error should be gone
3. **If still getting 403:**
   - Verify the token is fresh (tokens expire in 60 seconds)
   - Check that you assigned **client roles**, not realm roles
   - Make sure the client secret matches: `6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3`

## Quick Test

After assigning roles, test with:

```bash
# Get token
curl -X POST "http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=helaketha-agri-admin" \
  -d "client_secret=6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3"

# Use the token to create a user (should get 201 Created, not 403)
curl -X POST "http://localhost:8090/admin/realms/helakatha-agri-realm/users" \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "enabled": true
  }'
```

If you get `201 Created`, the permissions are correct! ✅

