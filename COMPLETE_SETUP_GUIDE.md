# Complete Keycloak Admin API Setup Guide

## 📋 Overview

This guide provides step-by-step instructions to:
1. Add Postman client to Keycloak realm
2. Create admin client with user management permissions
3. Integrate Keycloak Admin API to create users before saving to database
4. Store Keycloak user ID in database along with farmer information

---

## 📁 All Configuration File Paths

### Backend Configuration Files

1. **Keycloak Realm Configuration (JSON)**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json
   ```

2. **Application Configuration (YAML)**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\resources\application.yml
   ```

3. **Maven Dependencies (POM)**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\pom.xml
   ```

4. **Docker Compose**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\docker-compose.yml
   ```

### Java Source Files

5. **Keycloak Admin Service**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\KeycloakAdminService.java
   ```

6. **Farmer Service**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\FarmerService.java
   ```

7. **Farmer Controller**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\controller\FarmerController.java
   ```

8. **Farmer Entity**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\entity\Farmer.java
   ```

9. **Farmer Repository**
   ```
   C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\repository\FarmerRepositoryImpl.java
   ```

### Database Scripts

10. **Add Keycloak User ID Column**
    ```
    C:\Project\helaketha_agri_native\helaketha_agri_new\add-keycloak-user-id.sql
    ```

---

## 🚀 Step-by-Step Setup Instructions

### STEP 1: Start Keycloak and MySQL Services

```bash
cd C:\Project\helaketha_agri_native\helaketha_agri_new
docker-compose up -d
```

**Wait 1-2 minutes** for services to be healthy.

**Verify services are running:**
```bash
docker ps
```

You should see:
- `helaketha_agri_mysql` (port 3306)
- `helaketha_agri_keycloak` (port 8090)

---

### STEP 2: Access Keycloak Admin Console

1. Open browser: **http://localhost:8090**
2. Click **Administration Console**
3. Login:
   - Username: `admin`
   - Password: `admin`

---

### STEP 3: Import Realm Configuration

1. In Keycloak Admin Console, click the **realm dropdown** (top left, shows "master")
2. Click **"Create Realm"**
3. Click **"Import"** button
4. Select file: `C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json`
5. Click **"Create"**

**Realm Name:** `helakatha-agri-realm`

---

### STEP 4: Configure Admin Client and Get Client Secret

1. In Keycloak Admin Console, go to **`helakatha-agri-realm`** → **Clients**
2. Find **`helaketha-agri-admin`** client
3. Click on the client
4. Go to **"Credentials"** tab
5. **Copy the "Client secret"** (or click "Regenerate Secret" if needed)
6. **Save the secret** - you'll need it for `application.yml`

**Example Client Secret:** `a1b2c3d4-e5f6-7890-abcd-ef1234567890`

---

### STEP 5: Grant User Management Permissions to Admin Client

1. Still in **`helaketha-agri-admin`** client settings
2. Go to **"Service Account Roles"** tab
3. Click **"Assign role"**
4. In the filter, select **"Filter by clients"**
5. Select **`realm-management`**
6. Assign these roles:
   - ✅ **`manage-users`** - Create, update, delete users
   - ✅ **`view-users`** - View users
   - ✅ **`query-users`** - Search users
7. Click **"Assign"**

**Why this is needed:** The admin client needs these roles to create users via the Admin API.

---

### STEP 6: Update application.yml with Admin Client Secret

1. Open: `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\resources\application.yml`
2. Find the `keycloak` section
3. Update `admin-client-secret` with the secret you copied in Step 4:

```yaml
keycloak:
  server-url: http://localhost:8090
  realm: helakatha-agri-realm
  admin-client-id: helaketha-agri-admin
  admin-client-secret: YOUR_ACTUAL_CLIENT_SECRET_HERE  # ← Paste the secret here
```

**Save the file.**

---

### STEP 7: Add keycloak_user_id Column to Database

**Option A: Using MySQL Command Line**
```bash
mysql -u root -p helaketha_agri_db < C:\Project\helaketha_agri_native\helaketha_agri_new\add-keycloak-user-id.sql
```

**Option B: Using MySQL Workbench or phpMyAdmin**
1. Open the SQL script: `C:\Project\helaketha_agri_native\helaketha_agri_new\add-keycloak-user-id.sql`
2. Execute the SQL statements

**Option C: Manual SQL**
```sql
USE helaketha_agri_db;

ALTER TABLE farmers 
ADD COLUMN keycloak_user_id VARCHAR(255) NULL AFTER username;

CREATE INDEX idx_keycloak_user_id ON farmers(keycloak_user_id);
```

---

### STEP 8: Rebuild and Restart Application

1. **Rebuild Maven project:**
   ```bash
   cd C:\Project\helaketha_agri_native\helaketha_agri_new
   mvn clean install
   ```

2. **Restart Spring Boot application** (in your IDE or via command line)

---

## 🧪 Testing the Integration

### Test 1: Create a Farmer via API

**Request:**
```bash
POST http://localhost:8080/api/farmers
Content-Type: application/json

{
  "fullName": "John Doe",
  "phone": "0771234567",
  "email": "john.doe@example.com",
  "address": "123 Farm Street",
  "nic": "123456789V",
  "username": "johndoe",
  "password": "password123"
}
```

**Expected Flow:**
1. ✅ User created in Keycloak with username "johndoe"
2. ✅ User assigned "FARMER" role in Keycloak
3. ✅ Keycloak user ID retrieved
4. ✅ Farmer saved to database with `keycloak_user_id`
5. ✅ Response includes both `farmerId` and `keycloakUserId`

**Verify in Keycloak:**
- Go to Keycloak Admin Console → `helakatha-agri-realm` → **Users**
- You should see user "johndoe" with "FARMER" role

**Verify in Database:**
```sql
SELECT farmer_id, name, username, keycloak_user_id FROM farmers;
```

---

## 📮 Postman OAuth2 Configuration

### Setup Postman to Get Tokens

1. **Open Postman**
2. **Create a new request**
3. **Go to Authorization tab**
4. **Select "OAuth 2.0"**
5. **Configure:**

   - **Grant Type:** `Authorization Code`
   - **Callback URL:** `https://www.getpostman.com/oauth2/callback`
   - **Auth URL:** `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/auth`
   - **Access Token URL:** `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token`
   - **Client ID:** `helaketha-agri-postman`
   - **Client Secret:** (leave empty - it's a public client)
   - **Scope:** `openid profile email roles`
   - **State:** (leave empty)
   - **Client Authentication:** `Send as Basic Auth header`

6. **Click "Get New Access Token"**
7. **Login with Keycloak credentials**
8. **Token will be automatically added to requests**

### Test Postman Token

**Request:**
```
GET http://localhost:8080/api/farmers
Authorization: Bearer <token_from_postman>
```

---

## 🔑 Keycloak Clients Summary

### 1. helaketha-agri-backend
- **Type:** Confidential client
- **Purpose:** Backend API authentication
- **Redirect URIs:** `http://localhost:8080/*`

### 2. helaketha-agri-frontend
- **Type:** Public client
- **Purpose:** Frontend application
- **Redirect URIs:** `http://localhost:3000/*`, `http://localhost:3001/*`

### 3. helaketha-agri-postman
- **Type:** Public client
- **Purpose:** Postman OAuth2 testing
- **Redirect URI:** `https://www.getpostman.com/oauth2/callback`

### 4. helaketha-agri-admin
- **Type:** Confidential client (Service Account)
- **Purpose:** Admin API for user management
- **Service Account Enabled:** ✅ Yes
- **Required Roles:** `manage-users`, `view-users`, `query-users`

---

## 📦 Maven Dependencies

**Already added to pom.xml:**
```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-admin-client</artifactId>
    <version>25.0.0</version>
</dependency>
```

**File Path:**
```
C:\Project\helaketha_agri_native\helaketha_agri_new\pom.xml
```

---

## 🔄 How the Flow Works

### Creating a Farmer

```
┌─────────────┐
│  Frontend   │
│  (Next.js)  │
└──────┬──────┘
       │ POST /api/farmers
       │ { username, password, ... }
       ▼
┌─────────────────────┐
│ FarmerController    │
│ .addFarmer()        │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ FarmerService       │
│ .create()           │
└──────┬──────────────┘
       │
       ├─── Step 1: Create user in Keycloak
       │    ▼
       │ ┌──────────────────────┐
       │ │ KeycloakAdminService │
       │ │ .createUser()        │
       │ └──────┬───────────────┘
       │        │
       │        ▼ Keycloak API
       │ ┌──────────────────────┐
       │ │   Keycloak Server    │
       │ │   (Port 8090)        │
       │ └──────┬───────────────┘
       │        │
       │        ▼ Returns keycloakUserId
       │
       ├─── Step 2: Save to database
       │    ▼
       │ ┌──────────────────────┐
       │ │ FarmerRepository     │
       │ │ .insert()            │
       │ └──────┬───────────────┘
       │        │
       │        ▼ MySQL
       │ ┌──────────────────────┐
       │ │   MySQL Database     │
       │ │   (Port 3306)        │
       │ └──────────────────────┘
       │
       ▼
┌─────────────────────┐
│  Response           │
│  {                  │
│    farmerId: 1,    │
│    keycloakUserId: │
│    "uuid-here"     │
│  }                  │
└─────────────────────┘
```

---

## ✅ Verification Checklist

- [ ] Keycloak running on http://localhost:8090
- [ ] MySQL running on port 3306
- [ ] Realm `helakatha-agri-realm` imported
- [ ] Client `helaketha-agri-admin` exists
- [ ] Admin client secret copied to `application.yml`
- [ ] Admin client has `manage-users` role assigned
- [ ] `keycloak_user_id` column added to `farmers` table
- [ ] Maven dependencies downloaded
- [ ] Application rebuilt and restarted
- [ ] Test farmer creation works
- [ ] User appears in Keycloak
- [ ] User appears in database with `keycloak_user_id`

---

## 🐛 Troubleshooting

### Error: "User already exists in Keycloak"
**Solution:** Delete the user from Keycloak or use a different username

### Error: "Failed to create user in Keycloak: 403 Forbidden"
**Solution:** 
- Check admin client secret in `application.yml`
- Verify admin client has `manage-users` role in Service Account Roles

### Error: "Column 'keycloak_user_id' doesn't exist"
**Solution:** Run the SQL script: `add-keycloak-user-id.sql`

### Error: "Cannot connect to Keycloak"
**Solution:**
- Check Keycloak is running: `docker ps`
- Check Keycloak URL in `application.yml`: `http://localhost:8090`
- Verify Keycloak is accessible: http://localhost:8090

---

## 📝 Summary

✅ **Keycloak Realm Config:** `keycloak-realm-config.json`  
✅ **Application Config:** `src/main/resources/application.yml`  
✅ **Admin Client:** `helaketha-agri-admin` with `manage-users` role  
✅ **Postman Client:** `helaketha-agri-postman` with Postman redirect URL  
✅ **Database Column:** `keycloak_user_id` in `farmers` table  
✅ **Flow:** Create user in Keycloak → Get user ID → Save to database  

All paths and configurations are documented above. Follow the steps in order to complete the setup.

