CREATE TABLE IF NOT EXISTS chess.boards (
    gameID INTEGER NOT NULL,
    piece_row INTEGER NOT NULL,
    piece_col INTEGER NOT NULL,
    piece_type ENUM('PAWN', 'ROOK', 'KNIGHT', 'BISHOP', 'QUEEN', 'KING') NOT NULL,
    piece_color ENUM('WHITE', 'BLACK') NOT NULL,
    PRIMARY KEY (gameID, piece_row, piece_col)
);

CREATE TABLE IF NOT EXISTS chess.games (
    gameID INTEGER NOT NULL PRIMARY KEY,
    gameName VARCHAR(100) NOT NULL,
    whiteUsername VARCHAR(100) NULL,
    blackUsername VARCHAR(100) NULL
);

CREATE TABLE IF NOT EXISTS chess.sessions (
    authToken VARCHAR(100) NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS chess.users (
    username VARCHAR(100) NOT NULL PRIMARY KEY,
    hashed_password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);