# Keycloak Setup Guide for Helaketha Agriculture

## Overview
This application uses Keycloak for authentication. Passwords are NOT stored in the application database - they are managed by Keycloak. Only user IDs (usernames) are stored in the application database.

## Keycloak Realm Configuration Path

The Keycloak realm configuration file is located at:
```
C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json
```

## Keycloak Configuration in Docker Compose

Keycloak is configured to use MySQL database (same as your application database) in:
```
C:\Project\helaketha_agri_native\helaketha_agri_new\docker-compose.yml
```

## Setup Steps

### 1. Start Keycloak and MySQL

```bash
cd C:\Project\helaketha_agri_native\helaketha_agri_new
docker-compose up -d
```

Wait for services to be healthy (about 1-2 minutes).

### 2. Access Keycloak Admin Console

- URL: http://localhost:8090
- Username: `admin`
- Password: `admin`

### 3. Import Realm Configuration

**Option A: Using Keycloak Admin Console (Recommended)**
1. Log in to Keycloak Admin Console
2. Click on the realm dropdown (top left, shows "master")
3. Click "Create Realm"
4. Click "Import" button
5. Select the file: `keycloak-realm-config.json`
6. Click "Create"

**Option B: Using Keycloak CLI**
```bash
docker exec -it helaketha_agri_keycloak /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080 --realm master --user admin --password admin
docker exec -it helaketha_agri_keycloak /opt/keycloak/bin/kcadm.sh create realms -f /opt/keycloak/data/import/keycloak-realm-config.json
```

### 4. Create Users in Keycloak

After importing the realm, create users in Keycloak:

1. Go to Keycloak Admin Console → `helakatha-agri-realm` → Users
2. Click "Create new user"
3. Fill in:
   - Username: (e.g., "tractor_driver_1")
   - Email: (optional)
   - First Name, Last Name: (optional)
4. Go to "Credentials" tab
5. Set password and click "Set password"
6. Go to "Role Mappings" tab
7. Assign appropriate role (FARMER, TRACTOR_DRIVER, HARVESTER_DRIVER, FERTILIZER_SUPPLIER, or ADMIN)

### 5. Configure Client Secret (for backend client)

1. Go to Keycloak Admin Console → `helakatha-agri-realm` → Clients
2. Click on `helaketha-agri-backend`
3. Go to "Credentials" tab
4. Copy the "Client secret"
5. Update `application.yml` or environment variables with the client secret

## Database Schema

**Important**: The application database tables do NOT have a password column. Only username is stored:

- `tractor_drivers`: stores `username` (not password)
- `harvester_drivers`: stores `username` (not password)
- `fertilizer_suppliers`: stores `username` (not password)

The `username` field should match the username in Keycloak for authentication.

## Authentication Flow

1. User logs in through Keycloak (frontend or direct grant)
2. Keycloak returns JWT token
3. Frontend sends JWT token in Authorization header: `Bearer <token>`
4. Spring Boot validates JWT token with Keycloak
5. User is authenticated and authorized based on roles

## Keycloak Realm Configuration File Location

**Full Path:**
```
C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json
```

**Relative Path (from project root):**
```
./keycloak-realm-config.json
```

## Keycloak Data Directory

When Keycloak runs in Docker, the realm configuration can be imported from:
- Container path: `/opt/keycloak/data/import/`
- To import automatically on startup, mount the file:
  ```yaml
  volumes:
    - ./keycloak-realm-config.json:/opt/keycloak/data/import/keycloak-realm-config.json:ro
  ```

## Environment Variables

Keycloak connection is configured in `application.yml`:
```yaml
spring:
  security:
    oauth2:
      resource-server:
        jwt:
          issuer-uri: http://localhost:8090/realms/helakatha-agri-realm
```

## Testing Authentication

1. Get access token from Keycloak:
```bash
curl -X POST http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token \
  -d "client_id=helaketha-agri-backend" \
  -d "client_secret=your-client-secret" \
  -d "username=your-username" \
  -d "password=your-password" \
  -d "grant_type=password"
```

2. Use the token in API requests:
```bash
curl -H "Authorization: Bearer <access_token>" http://localhost:8080/api/tractor-drivers
```

