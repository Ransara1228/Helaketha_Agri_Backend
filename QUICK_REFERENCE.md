# Quick Reference - Keycloak Admin API Setup

## 🚀 Quick Start (5 Steps)

1. **Start Services:**
   ```bash
   cd C:\Project\helaketha_agri_native\helaketha_agri_new
   docker-compose up -d
   ```

2. **Import Realm:**
   - Go to http://localhost:8090
   - Login: admin/admin
   - Import: `keycloak-realm-config.json`

3. **Get Admin Client Secret:**
   - Keycloak → Clients → `helaketha-agri-admin` → Credentials tab
   - Copy secret

4. **Update application.yml:**
   - File: `src/main/resources/application.yml`
   - Update: `admin-client-secret: YOUR_SECRET`

5. **Grant Permissions:**
   - Keycloak → Clients → `helaketha-agri-admin` → Service Account Roles
   - Assign: `manage-users`, `view-users`, `query-users` from `realm-management`

6. **Add Database Column:**
   ```sql
   ALTER TABLE farmers ADD COLUMN keycloak_user_id VARCHAR(255) NULL AFTER username;
   ```

7. **Rebuild & Restart:**
   ```bash
   mvn clean install
   ```

---

## 📁 All File Paths

| File | Path |
|------|------|
| **Realm Config** | `C:\Project\helaketha_agri_native\helaketha_agri_new\keycloak-realm-config.json` |
| **Application Config** | `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\resources\application.yml` |
| **Docker Compose** | `C:\Project\helaketha_agri_native\helaketha_agri_new\docker-compose.yml` |
| **POM (Dependencies)** | `C:\Project\helaketha_agri_native\helaketha_agri_new\pom.xml` |
| **Keycloak Admin Service** | `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\KeycloakAdminService.java` |
| **Farmer Service** | `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\service\FarmerService.java` |
| **Farmer Controller** | `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\controller\FarmerController.java` |
| **Farmer Entity** | `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\entity\Farmer.java` |
| **Farmer Repository** | `C:\Project\helaketha_agri_native\helaketha_agri_new\src\main\java\com\helaketha\agri_new\agri\repository\FarmerRepositoryImpl.java` |
| **SQL Script** | `C:\Project\helaketha_agri_native\helaketha_agri_new\add-keycloak-user-id.sql` |

---

## 🔑 Keycloak Clients

| Client ID | Type | Purpose | Redirect URI |
|-----------|------|---------|--------------|
| `helaketha-agri-backend` | Confidential | Backend API | `http://localhost:8080/*` |
| `helaketha-agri-frontend` | Public | Frontend App | `http://localhost:3000/*` |
| `helaketha-agri-postman` | Public | Postman Testing | `https://www.getpostman.com/oauth2/callback` |
| `helaketha-agri-admin` | Confidential | Admin API | `http://localhost:8080/*` |

---

## 📝 Configuration Values

### application.yml
```yaml
keycloak:
  server-url: http://localhost:8090
  realm: helakatha-agri-realm
  admin-client-id: helaketha-agri-admin
  admin-client-secret: [GET FROM KEYCLOAK]
```

### Keycloak URLs
- **Admin Console:** http://localhost:8090
- **Realm:** http://localhost:8090/realms/helakatha-agri-realm
- **Token Endpoint:** http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token

---

## ✅ Verification

**Check Keycloak:**
- http://localhost:8090 → Login → Users → Should see created users

**Check Database:**
```sql
SELECT farmer_id, name, username, keycloak_user_id FROM farmers;
```

**Test API:**
```bash
POST http://localhost:8080/api/farmers
Content-Type: application/json

{
  "fullName": "Test User",
  "phone": "0771234567",
  "email": "test@example.com",
  "address": "123 Street",
  "nic": "123456789V",
  "username": "testuser",
  "password": "password123"
}
```

---

## 📚 Full Documentation

See `COMPLETE_SETUP_GUIDE.md` for detailed instructions.

