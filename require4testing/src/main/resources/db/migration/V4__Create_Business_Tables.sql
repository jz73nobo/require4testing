-- 先删除已存在的表（如果存在）
DROP TABLE IF EXISTS test_results CASCADE;
DROP TABLE IF EXISTS test_assignments CASCADE;
DROP TABLE IF EXISTS test_cases CASCADE;
DROP TABLE IF EXISTS test_runs CASCADE;
DROP TABLE IF EXISTS requirements CASCADE;

-- 需求表
CREATE TABLE requirements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 测试用例表
CREATE TABLE test_cases (
    id BIGSERIAL PRIMARY KEY,
    requirement_id BIGINT REFERENCES requirements(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    preconditions TEXT,
    expected_result TEXT,
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 测试运行表
CREATE TABLE test_runs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'PLANNED',
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 测试分配表
CREATE TABLE test_assignments (
    id BIGSERIAL PRIMARY KEY,
    test_run_id BIGINT REFERENCES test_runs(id) ON DELETE CASCADE,
    test_case_id BIGINT REFERENCES test_cases(id) ON DELETE CASCADE,
    assigned_to BIGINT REFERENCES users(id),
    status VARCHAR(50) DEFAULT 'ASSIGNED',
    assigned_at TIMESTAMPTZ DEFAULT NOW()
);

-- 测试结果表
CREATE TABLE test_results (
    id BIGSERIAL PRIMARY KEY,
    test_assignment_id BIGINT REFERENCES test_assignments(id) ON DELETE CASCADE,
    actual_result TEXT,
    status VARCHAR(50) DEFAULT 'NOT_TESTED',
    comments TEXT,
    tested_by BIGINT REFERENCES users(id),
    tested_at TIMESTAMPTZ DEFAULT NOW()
);

-- 创建索引提高查询性能
CREATE INDEX idx_test_cases_requirement_id ON test_cases(requirement_id);
CREATE INDEX idx_test_assignments_test_run_id ON test_assignments(test_run_id);
CREATE INDEX idx_test_assignments_assigned_to ON test_assignments(assigned_to);
CREATE INDEX idx_test_results_test_assignment_id ON test_results(test_assignment_id);