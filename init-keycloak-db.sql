-- Create Keycloak database
CREATE DATABASE IF NOT EXISTS keycloak CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Grant privileges to root user (already has access, but ensuring)
GRANT ALL PRIVILEGES ON keycloak.* TO 'root'@'%';
FLUSH PRIVILEGES;
