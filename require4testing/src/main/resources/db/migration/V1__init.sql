-- V1__init.sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100),
  role VARCHAR(30) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE requirements (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  created_by BIGINT REFERENCES users(id),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE test_cases (
  id BIGSERIAL PRIMARY KEY,
  requirement_id BIGINT REFERENCES requirements(id) ON DELETE CASCADE,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  created_by BIGINT REFERENCES users(id),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE test_runs (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  start_date DATE,
  end_date DATE,
  created_by BIGINT REFERENCES users(id),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE test_run_assignments (
  id BIGSERIAL PRIMARY KEY,
  test_run_id BIGINT REFERENCES test_runs(id) ON DELETE CASCADE,
  test_case_id BIGINT REFERENCES test_cases(id) ON DELETE CASCADE,
  assigned_tester_id BIGINT REFERENCES users(id),
  result VARCHAR(20),
  comment TEXT,
  executed_at TIMESTAMP WITH TIME ZONE
);

CREATE UNIQUE INDEX ux_test_run_case ON test_run_assignments(test_run_id, test_case_id);
