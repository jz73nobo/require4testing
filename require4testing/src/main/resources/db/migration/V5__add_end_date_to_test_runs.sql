-- V5__add_end_date_to_test_runs.sql
ALTER TABLE test_runs 
ADD COLUMN IF NOT EXISTS end_date DATE;