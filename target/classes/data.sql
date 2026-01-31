-- Initial users for the couple app
-- User 1: Thúi (nhatnam) - Male
-- User 2: Dịu (diuhien) - Female

-- First, update existing users if they exist
UPDATE users SET username = 'nhatnam', name = 'Thúi 💙', pin = '050504', partner_id = 2 WHERE id = 1;
UPDATE users SET username = 'diuhien', name = 'Dịu 💗', pin = '191106', partner_id = 1 WHERE id = 2;

-- Insert if not exists
INSERT INTO users (id, username, name, pin, avatar, couple_start_date, partner_id) 
SELECT 1, 'nhatnam', 'Thúi 💙', '050504', NULL, '2024-01-01', 2
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);

INSERT INTO users (id, username, name, pin, avatar, couple_start_date, partner_id) 
SELECT 2, 'diuhien', 'Dịu 💗', '191106', NULL, '2024-01-01', 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 2);
