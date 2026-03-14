-- SQL Script to Add Keycloak User ID Column
-- Run this script in your MySQL database

USE helaketha_agri_db;

-- Add keycloak_user_id column to farmers table
ALTER TABLE farmers 
ADD COLUMN keycloak_user_id VARCHAR(255) NULL AFTER username;

-- Optional: Add index for faster lookups
CREATE INDEX idx_keycloak_user_id ON farmers(keycloak_user_id);

-- Verify the column was added
DESCRIBE farmers;

