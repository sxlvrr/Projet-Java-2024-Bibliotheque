import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la gestion de la connexion à la base de données.
 */
public class Database {
    // Informations de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost/projet_bibliotheque";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Établit la connexion à la base de données.
     * @return Connexion établie
     * @throws SQLException En cas d'erreur lors de la connexion
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC introuvable", e);
        }
    }

    /**
     * Ferme la connexion à la base de données.
     * @param connection Connexion à fermer
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    /**
     * Ferme les ressources JDBC (connexion, statement, resultSet).
     * @param connection Connexion à fermer
     * @param statement Statement à fermer
     * @param resultSet ResultSet à fermer
     */
    public static void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
        }
    }
}
