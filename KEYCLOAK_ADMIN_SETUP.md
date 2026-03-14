# Keycloak Admin API Setup Guide

## Overview
This guide explains how to set up Keycloak Admin API integration to create users in Keycloak before saving them to the database.

## Step-by-Step Setup Instructions

### Step 1: Start Keycloak and MySQL

```bash
cd C:\Project\helaketha_agri_native\helaketha_agri_new
docker-compose up -d
```

Wait for services to be healthy (about 1-2 minutes).

### Step 2: Access Keycloak Admin Console

- URL: http://localhost:8090
- Username: `admin`
- Password: `admin`

### Step 3: Import Realm Configuration

1. Log in to Keycloak Admin Console
2. Click on the realm dropdown (top left, shows "master")
3. Click "Create Realm"
4. Click "Import" button
5. Select the file: `keycloak-realm-config.json`
6. Click "Create"

**Realm Configuration File Path:**
```
C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json
```

### Step 4: Configure Admin Client and Get Client Secret

1. Go to Keycloak Admin Console → `helakatha-agri-realm` → **Clients**
2. Find `helaketha-agri-admin` client (or create it if not imported)
3. Click on the client
4. Go to **Credentials** tab
5. Copy the **Client secret** (or generate one if not set)
6. Update `application.yml` with the actual secret:

**File Path:**
```
C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\resources\application.yml
```

Update this line:
```yaml
keycloak:
  admin-client-secret: YOUR_ACTUAL_CLIENT_SECRET_HERE
```

### Step 5: Grant User Management Permissions to Admin Client

1. Go to Keycloak Admin Console → `helakatha-agri-realm` → **Clients**
2. Click on `helaketha-agri-admin`
3. Go to **Service Account Roles** tab
4. Click **Assign role**
5. Filter by **Filter by clients** and select `realm-management`
6. Assign these roles:
   - `manage-users` - Create, update, delete users
   - `view-users` - View users
   - `query-users` - Search users
7. Click **Assign**

### Step 6: Add keycloak_user_id Column to Database

Run this SQL script to add the Keycloak user ID column:

```sql
USE helaketha_agri_db;

-- Add keycloak_user_id column to farmers table
ALTER TABLE farmers 
ADD COLUMN keycloak_user_id VARCHAR(255) NULL AFTER username;

-- Optional: Add index for faster lookups
CREATE INDEX idx_keycloak_user_id ON farmers(keycloak_user_id);
```

**SQL Script File Path:**
```
C:\Project\helaketha_agri_native\helaketha_agri_new\add-keycloak-user-id.sql
```

### Step 7: Rebuild and Restart Application

1. Rebuild your Java project:
   ```bash
   cd C:\Project\helaketha_agri_native\helaketha_agri_new
   mvn clean install
   ```

2. Restart your Spring Boot application

## Configuration Files and Paths

### 1. Keycloak Realm Configuration
**Path:** `C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json`

This file contains:
- Realm: `helakatha-agri-realm`
- Clients:
  - `helaketha-agri-backend` - Backend API client
  - `helaketha-agri-frontend` - Frontend client
  - `helaketha-agri-postman` - Postman client (redirect: https://www.getpostman.com/oauth2/callback)
  - `helaketha-agri-admin` - Admin client for user management

### 2. Application Configuration
**Path:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\resources\application.yml`

Contains:
- Keycloak server URL
- Realm name
- Admin client ID and secret

### 3. Keycloak Admin Service
**Path:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\KeycloakAdminService.java`

Service for:
- Creating users in Keycloak
- Assigning roles
- Deleting users
- Updating passwords

### 4. Docker Compose
**Path:** `C:\Project\helaketha_agri_native\helaketha_agri_new\docker-compose.yml`

Contains:
- MySQL configuration
- Keycloak configuration with auto-import

## How It Works

### Creating a Farmer

1. **Frontend sends request** with farmer data including password
2. **Backend receives request** in `FarmerController.addFarmer()`
3. **FarmerService.create()** is called:
   - **Step 1:** Create user in Keycloak via `KeycloakAdminService.createUser()`
   - **Step 2:** Get Keycloak user ID from response
   - **Step 3:** Save farmer to database with `keycloak_user_id`
4. **Response** returns farmer with both `farmerId` and `keycloakUserId`

### Flow Diagram

```
Frontend Request (with password)
    ↓
FarmerController.addFarmer()
    ↓
FarmerService.create(farmer, password)
    ↓
KeycloakAdminService.createUser() → Keycloak
    ↓
Get keycloakUserId
    ↓
Save to Database (with keycloakUserId)
    ↓
Return Farmer (with farmerId + keycloakUserId)
```

## Testing with Postman

### 1. Get Access Token (OAuth2)

**Request:**
```
POST http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
&client_id=helaketha-agri-postman
&username=your-username
&password=your-password
```

**Response:**
```json
{
  "access_token": "...",
  "token_type": "Bearer",
  "expires_in": 300
}
```

### 2. Use Token in API Requests

**Request:**
```
GET http://localhost:8080/api/farmers
Authorization: Bearer <access_token>
```

## Postman OAuth2 Configuration

1. Open Postman
2. Create a new request
3. Go to **Authorization** tab
4. Select **OAuth 2.0**
5. Configure:
   - **Grant Type:** Authorization Code
   - **Callback URL:** https://www.getpostman.com/oauth2/callback
   - **Auth URL:** http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/auth
   - **Access Token URL:** http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token
   - **Client ID:** helaketha-agri-postman
   - **Client Secret:** (leave empty for public client)
   - **Scope:** openid profile email roles
6. Click **Get New Access Token**
7. Login with Keycloak credentials
8. Token will be automatically added to requests

## Keycloak Admin Client Dependencies

**Maven Dependency (already added to pom.xml):**
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

## Database Schema Update

**SQL Script Path:**
```
C:\Project\helaketha_agri_native\helaketha_agri_new\add-keycloak-user-id.sql
```

Run this to add `keycloak_user_id` column to `farmers` table.

## Troubleshooting

### Issue: "User already exists in Keycloak"
- Check if username already exists in Keycloak
- Use a different username or delete existing user

### Issue: "Failed to create user in Keycloak"
- Check Keycloak admin client secret in `application.yml`
- Verify admin client has `manage-users` role assigned
- Check Keycloak server is running and accessible

### Issue: "Column 'keycloak_user_id' doesn't exist"
- Run the SQL script to add the column
- Restart the application

## Summary of All Configuration Paths

1. **Keycloak Realm Config:** `C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json`
2. **Application Config:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\resources\application.yml`
3. **Docker Compose:** `C:\Project\helaketha_agri_native\helaketha_agri_new\docker-compose.yml`
4. **Maven POM:** `C:\Project\helaketha_agri_native\helaketha_agri_new\pom.xml`
5. **Keycloak Admin Service:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\KeycloakAdminService.java`
6. **Farmer Service:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\FarmerService.java`
7. **Farmer Controller:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\controller\FarmerController.java`
8. **Farmer Repository:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\repository\FarmerRepositoryImpl.java`
9. **Farmer Entity:** `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\entity\Farmer.java`

