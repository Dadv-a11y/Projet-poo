--
-- Fichier généré par SQLiteStudio v3.4.4 sur lun. mai 19 12:57:45 2025
--
-- Encodage texte utilisé : UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: books
CREATE TABLE IF NOT EXISTS books (
    book_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    isbn TEXT UNIQUE,
    description TEXT NOT NULL,
    category_id INTEGER,
    published_year INTEGER,
    copies_total INTEGER NOT NULL DEFAULT 1 CHECK (copies_total > 0),
    copies_available INTEGER NOT NULL DEFAULT 0 CHECK (copies_available >= 0),
    created_at DATE DEFAULT (datetime('now')),
    image_path TEXT,
    FOREIGN KEY (category_id) REFERENCES category (category_id) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Table: category
CREATE TABLE IF NOT EXISTS category (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT
);

-- Table: loans
CREATE TABLE IF NOT EXISTS loans (
    loan_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    borrowed_at DATE DEFAULT (datetime('now')),
    due_at DATE,
    returned_at DATE,
    status TEXT NOT NULL DEFAULT 'ONGOING' CHECK (status IN ('ONGOING', 'RETURNED', 'OVERDUE')),
    number_of_book INTEGER NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY ASC AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('ADMIN', 'MEMBER')),
    created_at DATE DEFAULT (datetime('now')),
    phone INTEGER,
    birthdate TEXT,
    gender TEXT CHECK (gender IN ('Male', 'Female')),
    address TEXT,
    email TEXT NOT NULL UNIQUE
);

-- Example admin and member users
INSERT INTO users (user_id, username, password_hash, role, created_at, phone, birthdate, gender, address, email) VALUES (1, 'admin', '$2a$12$bhaJOc2G83.9.OX4w1wwkuaRFkxwvcIFb7JQ9EQCABVzewLVh6nSK', 'ADMIN', '2025-04-25 20:22:30', 0, '1700-01-01', NULL, NULL, 'admin@mail.com');
-- INSERT INTO users (user_id, username, password_hash, role, created_at, phone, birthdate, gender, address, email) VALUES (2, 'Chris james', '$2a$12$wJfpFT9ulr12FA6SEN/cgOkDBGhbEcVNC.LB3agCRXHFxeWDLk4cW', 'MEMBER', '2025-05-06 02:42:43', 699123456, '1995-06-12', 'Male', 'Douala', 'chris@mail.com');
-- INSERT INTO users (user_id, username, password_hash, role, created_at, phone, birthdate, gender, address, email) VALUES (3, 'Anna Smith', '$2a$12$Ec4uveRy0eXZPDKH1b35SOMFLYu30YlSYEBGoYsBFq7YE4OliUqPO', 'MEMBER', '2025-05-06 02:43:37', 678987654, '1998-03-22', 'Female', 'Yaounde', 'anna@mail.com');

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_books_title ON books (title);
CREATE INDEX IF NOT EXISTS idx_category_name ON category(name);
CREATE INDEX IF NOT EXISTS idx_loans_book ON loans (book_id);
CREATE INDEX IF NOT EXISTS idx_loans_user_status ON loans (user_id, status);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users (email);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;

-- =========================================================
--  ADMIN IDENTIFIER
-- =========================================================
--  password "admin123" Username "admin@mail.com"