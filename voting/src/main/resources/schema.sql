CREATE DATABASE IF NOT EXISTS votingsys;

-- CREATE USER 'adminn'@'localhost' IDENTIFIED BY 'adminnn';

-- Grant access to adminn for votingsys database
GRANT ALL PRIVILEGES ON votingsys.* TO 'adminn'@'localhost';

-- Make sure the privileges are reloaded
FLUSH PRIVILEGES;