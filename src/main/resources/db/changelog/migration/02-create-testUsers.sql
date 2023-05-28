-- liquibase formatter sql

-- changeset andrewZ:1
INSERT INTO users (login,password)
VALUES ('user1@mail.ru','$2a$12$kCZ8skv2owk8n3jZwoV6zOIKZCgMMA5MYSsVvDauXdUOVstXLNudG');
INSERT INTO users (login,password)
VALUES ('user2@mail.ru','$2a$12$kCZ8skv2owk8n3jZwoV6zOIKZCgMMA5MYSsVvDauXdUOVstXLNudG');
-- rollback TRUNCATE users;