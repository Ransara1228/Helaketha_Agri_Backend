-- Create Keycloak database
CREATE DATABASE IF NOT EXISTS keycloak_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Grant privileges
GRANT ALL PRIVILEGES ON keycloak_db.* TO 'root'@'%';
FLUSH PRIVILEGES;
