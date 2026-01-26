# How to Find and Configure the Service Account User

## Understanding Service Accounts

When you enable "Service accounts enabled" on a client, Keycloak automatically creates a special system user for that client. This user is named: `service-account-{client-id}`

For your client `helaketha-agri-admin`, the service account user is: `service-account-helaketha-agri-admin`

## Method 1: Find Service Account via Users Page

1. **Go to Users** (you're already there)
2. **Look for a filter or search option** - There should be a way to filter users
3. **Search for:** `service-account-helaketha-agri-admin`
   - Or look for a **"Service accounts"** tab/button
   - Or use the search box and type: `service-account`

4. **If you see it**, click on it to open the user details
5. **Go to "Role mapping" tab**
6. **Click "Assign role"**
7. **Filter by clients** → Select `realm-management`
8. **Assign the roles:** `manage-users`, `view-users`, `query-users`

## Method 2: Assign Roles Directly from Client Settings (EASIER)

This is the **recommended method**:

1. **Go to Clients** (in the left sidebar)
2. **Click on:** `helaketha-agri-admin`
3. **Click on:** **"Service account roles"** tab
4. **Click "Assign role"** button (with dropdown arrow)
5. **Select "Filter by clients"**
6. **Select:** `realm-management`
7. **Check the boxes for:**
   - ✅ `manage-users`
   - ✅ `view-users`
   - ✅ `query-users`
8. **Click "Assign"**

This method is easier because you don't need to find the service account user manually!

## Method 3: Verify Service Account Exists

If you want to verify the service account exists:

1. Go to **Users**
2. In the search box, type: `service-account-helaketha-agri-admin`
3. Press Enter or click search
4. If it exists, it should appear in the results

**Note:** Some Keycloak versions hide service accounts by default. If you can't find it, use **Method 2** instead.

## What Happens Next

Once you assign the `realm-management` client roles:

1. ✅ Your Spring Boot application can create users via Keycloak Admin API
2. ✅ When you create a farmer in your application, it will:
   - Create the user in Keycloak automatically
   - Generate a temporary password
   - Store the Keycloak user ID in your database
3. ✅ Users will appear in Keycloak Users list (created by your app)
4. ✅ Users can login to Keycloak with their username and temporary password

## Testing

After assigning roles:

1. **Restart your Spring Boot application**
2. **Try creating a farmer** through your frontend
3. **Check Keycloak Users** - you should see the new user appear
4. **The user can login** to Keycloak with username and temporary password

## Important Notes

- **You don't need to create users manually** - your application will create them
- **The service account is a system user** - it's used by your backend to manage users
- **Regular users will be created automatically** when you add farmers through your application
- **The "No users found" message is normal** - users will appear as you create them through your app

