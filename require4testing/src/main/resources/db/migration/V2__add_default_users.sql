-- V2__add_default_users.sql
-- 添加默认用户数据

-- BCrypt加密的密码都是 "password"
INSERT INTO users (username, password, full_name, role, created_at) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'ADMIN', 'ADMIN', NOW()),
('req_engineer', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'REQUIREMENTS_ENGINEER', 'REQUIREMENTS_ENGINEER', NOW()),
('test_manager', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'TEST_MANAGER', 'TEST_MANAGER', NOW()),
('test_designer', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'TEST_DESIGNER', 'TEST_DESIGNER', NOW()),
('tester1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'TESTER1', 'TESTER', NOW()),
('tester2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi', 'TESTER2', 'TESTER', NOW());

-- 可选：添加一些测试数据
INSERT INTO requirements (title, description, created_by, created_at) VALUES
('User Login', 'Users should be able to log in to the system using a username and password', 2, NOW()),
('Test Case Management', 'Ability to create, edit, and delete test cases', 2, NOW());