package dataaccess.database;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.ColorAlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.UnknownColorException;
import dataaccess.interfaces.GameDAO;
import dataaccess.memory.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.*;

public class DatabaseGameDAO implements GameDAO {

    private final GameDAO cache;
    private final String table;
    private final String board_table;
    private int nextGameID;
    //Look into Column index (so it puts in buckets) (if need for faster)
    public DatabaseGameDAO() throws Exception {
        cache = new MemoryGameDAO();
        table = "games";
        board_table = "boards";
        DatabaseManager.createDatabase();
        String games_sql = """
                CREATE TABLE IF NOT EXISTS games (
                    gameID INTEGER NOT NULL PRIMARY KEY,
                    gameName VARCHAR(100) NOT NULL,
                    whiteUsername VARCHAR(100) NULL,
                    blackUsername VARCHAR(100) NULL
                );
                """;
        String boards_sql = """
                CREATE TABLE IF NOT EXISTS boards (
                    gameID INTEGER NOT NULL,
                    piece_row INTEGER NOT NULL,
                    piece_col INTEGER NOT NULL,
                    piece_type ENUM('PAWN', 'ROOK', 'KNIGHT', 'BISHOP', 'QUEEN', 'KING') NOT NULL,
                    piece_color ENUM('WHITE', 'BLACK') NOT NULL,
                    PRIMARY KEY (gameID, piece_row, piece_col)
                );
                """;
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(games_sql)) {
                statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement(boards_sql)) {
                statement.executeUpdate();
            }
        }
        String sql = "SELECT gameID FROM " + table + " ORDER gameID asc LIMIT 1;";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                try (var results = statement.executeQuery()) {
                    nextGameID = results.getInt("gameID");
                }
            }
        } catch (Exception e) {
            nextGameID = 1;
        }
    }

    public Collection<GameData> listGames()  throws Exception {
        List<Integer> gameIDs = new ArrayList<>();
        String sql = "SELECT gameID FROM " + table + ";";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                try (var results = statement.executeQuery()) {
                    while (results.next()) {
                        gameIDs.add(results.getInt("gameID"));
                    }
                }
            }
        }
        Collection<GameData> games = new LinkedList<>();
        for (Integer gameID : gameIDs) {
            GameData game = getGame(gameID);
            games.add(game);
        }
        return games;
    }

    public GameData getGame(int gameID) throws Exception {
        GameData cache_result = cache.getGame(gameID);
        if (cache_result != null) {
            return cache_result;
        }
        String main_sql = "SELECT gameName, whiteUsername, blackUsername FROM " + table + " WHERE gameID = ? LIMIT 1;";
        String table_sql = "SELECT piece_row, piece_col, piece_type, piece_color FROM " + board_table + " WHERE gameID = ?;";
        try (var conn = DatabaseManager.getConnection()) {
            ChessBoard board = new ChessBoard();
            try (var statement = conn.prepareStatement(table_sql)) {
                statement.setInt(1, gameID);
                try (var results = statement.executeQuery()) {
                    while (results.next()) {
                        int piece_row = results.getInt("piece_row");
                        int piece_col = results.getInt("piece_col");
                        String typeStr = results.getString("piece_type");
                        String colorStr = results.getString("piece_color");
                        ChessPiece.PieceType piece_type = ChessPiece.PieceType.valueOf(typeStr);
                        ChessGame.TeamColor piece_color = ChessGame.TeamColor.valueOf(colorStr);
                        board.addPiece(new ChessPosition(piece_row, piece_col), new ChessPiece(piece_color, piece_type)
                        );
                    }
                }
            }
            try (var statement = conn.prepareStatement(main_sql)) {
                statement.setInt(1, gameID);
                try (var results = statement.executeQuery()) {
                    if (results.next()) {
                        String gameName = results.getString("gameName");
                        String whiteUsername = results.getString("whiteUsername");
                        String blackUsername = results.getString("blackUsername");
                        ChessGame loadedGame = new ChessGame();
                        loadedGame.setBoard(board);
                        GameData game = new GameData(gameID,whiteUsername,blackUsername,gameName,loadedGame);
                        cache.addGame(game);
                        return game;
                    }
                }
            }
        }
        return null;
    }

    public void joinGame(int gameID, String playerColor, String username) throws Exception {
        GameData game = getGame(gameID);
        if (game == null) {
            throw new DataAccessException("No Game with given ID");
        }
        if ("WHITE".equals(playerColor)) {
            if (game.getWhiteUsername() != null && !game.getWhiteUsername().isEmpty()) {
                throw new ColorAlreadyTakenException("White is already taken");
            }
        } else if ("BLACK".equals(playerColor)) {
            if (game.getBlackUsername() != null && !game.getBlackUsername().isEmpty()) {
                throw new ColorAlreadyTakenException("Black is already taken");
            }
        }
        forceJoinGame(gameID, playerColor, username);
    }
    public void forceJoinGame(int gameID, String playerColor, String username) throws Exception {
        if (!"WHITE".equals(playerColor) && !"BLACK".equals(playerColor)) {
            throw new UnknownColorException("Unknown Color");
        }

        String sql = "UPDATE " + table + " SET " + ("WHITE".equals(playerColor) ? "whiteUsername" : "blackUsername") + " = ? WHERE gameID = ?;";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setInt(2, gameID);
                statement.executeUpdate();
            }
        }
        cache.forceJoinGame(gameID,playerColor,username);
    }

    public int createGame(String gameName) throws Exception {
        int gameID = nextGameID++;
        GameData newGame = new GameData(gameID, gameName, new ChessGame());
        addGame(newGame);
        return gameID;
    }

    public void addGame(GameData game) throws Exception {

        String sql = "INSERT INTO " + table + " (gameID, gameName, whiteUsername, blackUsername) VALUES (?, ?, ?, ?);";
        String board_sql = " INSERT INTO " + board_table + " (gameID, piece_row, piece_col, piece_type, piece_color) VALUES (?, ?, ?, ?, ?);";
        try (var conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false); // We need to ensure it ONLY commits it if EVERY sql command is a success
            try {
                try (var statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, game.getGameID());
                    statement.setString(2, game.getGameName());
                    statement.setString(3, game.getWhiteUsername());
                    statement.setString(4, game.getBlackUsername());
                    statement.executeUpdate();
                }
                for (Map.Entry<ChessPosition, ChessPiece> entry : game.getGame().getBoard()) {
                    ChessPosition position = entry.getKey();
                    ChessPiece piece = entry.getValue();
                    try (var statement = conn.prepareStatement(board_sql)) {
                        statement.setInt(1, game.getGameID());
                        statement.setInt(2, position.getRow());
                        statement.setInt(3, position.getColumn());
                        statement.setString(4,piece.getPieceType().name());
                        statement.setString(5,piece.getTeamColor().name());
                        statement.executeUpdate();
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
            }
        }
        cache.addGame(game);
    }

    public void reset() throws Exception {
        String sql = "TRUNCATE " + table + ";";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }
        String board_sql = "TRUNCATE " + board_table + ";";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(board_sql)) {
                statement.executeUpdate();
            }
        }
        cache.reset();
    }

    public void resetCache() throws Exception {
        cache.reset();
    }
}
