package com.helaketha.agri_new.agri.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.server-url:http://localhost:8090}")
    private String serverUrl;

    @Value("${keycloak.realm:helakatha-agri-realm}")
    private String realm;

    @Value("${keycloak.admin-client-id:helaketha-agri-admin}")
    private String clientId;

    @Value("${keycloak.admin-client-secret:admin-client-secret-change-me}")
    private String clientSecret;

    /**
     * Get Keycloak admin client instance
     */
    private Keycloak getKeycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    /**
     * Create a new user in Keycloak with auto-generated temporary password
     * @param username Username for the user
     * @param email Email address
     * @param firstName First name
     * @param lastName Last name
     * @param role Role to assign (e.g., "FARMER", "TRACTOR_DRIVER", etc.)
     * @return Keycloak user ID if successful
     */
    public String createUser(String username, String email, String firstName, String lastName, String role) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setEmailVerified(true);

            // Create user
            Response response = usersResource.create(user);

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                // Get the user ID from the location header
                String userId = getUserIdFromLocation(response.getLocation().getPath());

                // Generate and set temporary password (user must change on first login)
                String temporaryPassword = generateTemporaryPassword();
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(temporaryPassword);
                credential.setTemporary(true); // User must change password on first login

                usersResource.get(userId).resetPassword(credential);

                // Assign role
                if (role != null && !role.isEmpty()) {
                    assignRoleToUser(userId, role);
                }

                return userId;
            } else if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                // User already exists - try to find and return existing user ID
                try {
                    List<UserRepresentation> existingUsers = usersResource.search(username, true);
                    if (!existingUsers.isEmpty()) {
                        return existingUsers.get(0).getId();
                    }
                } catch (Exception searchEx) {
                    // If search fails (403), just throw the conflict error
                }
                throw new RuntimeException("User with username '" + username + "' already exists in Keycloak");
            } else {
                String errorMessage = response.readEntity(String.class);
                throw new RuntimeException("Failed to create user in Keycloak (Status: " + response.getStatus() + "): " + errorMessage);
            }
        } catch (RuntimeException e) {
            // Re-throw runtime exceptions as-is
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Generate a secure temporary password for new users
     */
    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        // Ensure at least one uppercase, one lowercase, one digit, one special char
        password.append(chars.charAt(random.nextInt(26))); // Uppercase
        password.append(chars.charAt(26 + random.nextInt(26))); // Lowercase
        password.append(chars.charAt(52 + random.nextInt(10))); // Digit
        password.append(chars.charAt(62 + random.nextInt(8))); // Special char
        
        // Fill the rest randomly (total length 12)
        for (int i = 4; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }

    /**
     * Assign a realm role to a user
     */
    private void assignRoleToUser(String userId, String roleName) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            
            // Get the role
            var role = realmResource.roles().get(roleName).toRepresentation();
            
            // Assign role to user
            realmResource.users().get(userId).roles().realmLevel()
                    .add(Collections.singletonList(role));
        } catch (Exception e) {
            // Log error but don't fail user creation
            System.err.println("Warning: Failed to assign role '" + roleName + "' to user: " + e.getMessage());
        }
    }

    /**
     * Extract user ID from Keycloak location header
     */
    private String getUserIdFromLocation(String location) {
        // Location format: /admin/realms/{realm}/users/{userId}
        String[] parts = location.split("/");
        return parts[parts.length - 1];
    }

    /**
     * Delete a user from Keycloak
     */
    public void deleteUser(String keycloakUserId) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            realmResource.users().get(keycloakUserId).remove();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user from Keycloak: " + e.getMessage(), e);
        }
    }

    /**
     * Update user password in Keycloak
     */
    public void updateUserPassword(String keycloakUserId, String newPassword) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);

            realmResource.users().get(keycloakUserId).resetPassword(credential);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user password in Keycloak: " + e.getMessage(), e);
        }
    }
}

