-- V6__add_fields_to_test_cases.sql
ALTER TABLE test_cases 
ADD COLUMN IF NOT EXISTS preconditions TEXT,
ADD COLUMN IF NOT EXISTS expected_result TEXT,
ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'DRAFT',
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT NOW();

-- 创建索引提高查询性能
CREATE INDEX IF NOT EXISTS idx_test_cases_requirement_id ON test_cases(requirement_id);
CREATE INDEX IF NOT EXISTS idx_test_cases_created_by ON test_cases(created_by);
CREATE INDEX IF NOT EXISTS idx_test_cases_status ON test_cases(status);