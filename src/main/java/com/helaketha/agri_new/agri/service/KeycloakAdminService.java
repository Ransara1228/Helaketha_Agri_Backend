package com.helaketha.agri_new.agri.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.server-url:http://localhost:8090}")
    private String serverUrl;

    @Value("${keycloak.realm:helakatha-agri-realm}")
    private String realm;

    @Value("${keycloak.admin-client-id:helaketha-agri-admin}")
    private String clientId;

    @Value("${keycloak.admin-client-secret:6eEij10r2wW7JdmNYssnZQ2eu8pFjJi3}")
    private String clientSecret;

    private Keycloak getKeycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    public String createUser(String username, String email, String firstName, String lastName, String role) {
        Keycloak keycloak = getKeycloakAdminClient();
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setEmailVerified(true);

            try (Response response = usersResource.create(user)) {
                if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                    String userId = getUserIdFromLocation(response.getLocation().getPath());

                    // Generate and set a secure random password
                    updateUserPassword(userId, generateTemporaryPassword());

                    // Assign the specific role
                    assignRole(userId, role);

                    return userId;
                } else if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                    throw new RuntimeException("User with username '" + username + "' already exists in Keycloak");
                } else {
                    throw new RuntimeException("Keycloak error: " + response.getStatus());
                }
            }
        } finally {
            keycloak.close(); // Prevent resource leaks
        }
    }

    public UserProvisioningResult createOrLinkUserWithRole(
            String username,
            String email,
            String firstName,
            String lastName,
            String role
    ) {
        try {
            String userId = createUser(username, email, firstName, lastName, role);
            return new UserProvisioningResult(userId, true);
        } catch (RuntimeException ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if (!message.contains("already exists")) {
                throw ex;
            }
        }

        String existingUserId = findUserIdByUsernameOrEmail(username, email);
        if (existingUserId == null || existingUserId.isBlank()) {
            throw new RuntimeException("User exists in Keycloak but could not be linked by username/email");
        }

        assignRole(existingUserId, role);
        return new UserProvisioningResult(existingUserId, false);
    }

    public String getUserIdByUsername(String username) {
        Keycloak keycloak = getKeycloakAdminClient();
        try {
            List<UserRepresentation> users = keycloak.realm(realm).users().search(username, true);
            if (users.isEmpty()) {
                throw new RuntimeException("User not found in Keycloak: " + username);
            }
            return users.get(0).getId();
        } finally {
            keycloak.close();
        }
    }

    public String findUserIdByUsernameOrEmail(String username, String email) {
        Keycloak keycloak = getKeycloakAdminClient();
        try {
            UsersResource usersResource = keycloak.realm(realm).users();

            List<UserRepresentation> usernameMatches = usersResource.search(username, true);
            for (UserRepresentation user : usernameMatches) {
                if (username.equalsIgnoreCase(user.getUsername())) {
                    return user.getId();
                }
            }

            if (email != null && !email.isBlank()) {
                List<UserRepresentation> emailMatches = usersResource.searchByEmail(email, true);
                for (UserRepresentation user : emailMatches) {
                    if (email.equalsIgnoreCase(user.getEmail())) {
                        return user.getId();
                    }
                }
            }
            return null;
        } finally {
            keycloak.close();
        }
    }

    public void assignRole(String userId, String roleName) {
        Keycloak keycloak = getKeycloakAdminClient();
        try {
            RealmResource realmResource = keycloak.realm(realm);

            // 1. Check if the role exists in the realm
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();

            // 2. Assign the role to the user
            realmResource.users().get(userId).roles().realmLevel()
                    .add(Collections.singletonList(role));

        } catch (Exception e) {
            System.err.println("Warning: Failed to assign role '" + roleName + "' to user. Error: " + e.getMessage());
        } finally {
            keycloak.close();
        }
    }

    public record UserProvisioningResult(String userId, boolean created) {
        public UserProvisioningResult {
            userId = Objects.requireNonNull(userId, "userId cannot be null");
        }
    }

    public void updateUserPassword(String userId, String password) {
        Keycloak keycloak = getKeycloakAdminClient();
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(true); // User must change password on first login

            keycloak.realm(realm).users().get(userId).resetPassword(credential);
        } finally {
            keycloak.close();
        }
    }

    public void deleteUser(String userId) {
        Keycloak keycloak = getKeycloakAdminClient();
        try {
            keycloak.realm(realm).users().get(userId).remove();
        } finally {
            keycloak.close();
        }
    }

    /**
     * Generates a random secure password
     */
    public String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();

        // Ensure standard length
        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private String getUserIdFromLocation(String location) {
        // Expected format: .../users/{userId}
        String[] parts = location.split("/");
        return parts[parts.length - 1];
    }
}