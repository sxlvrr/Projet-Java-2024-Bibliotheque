import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    // Informations de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost/projet_bibliotheque";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Noms de colonnes
    private static final String USER_EMAIL_COLUMN = "email";
    private static final String USER_PASSWORD_COLUMN = "password";

    // Méthode pour établir la connexion à la base de données
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC introuvable", e);
        }
    }

    // Méthode pour fermer la connexion à la base de données
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    
    // Méthode pour fermer les ressources
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

    // Méthode générique pour récupérer toutes les données d'une table
    public static <T> List<T> getAllData(Class<T> clazz, String tableName) {
        List<T> dataList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                T data = clazz.getDeclaredConstructor().newInstance();

                // Utilisation de la réflexion pour obtenir les noms des champs
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    if (value != null) {
                        field.setAccessible(true);
                        field.set(data, value);
                    }
                }

                dataList.add(data);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            System.err.println("Erreur lors de la récupération des données : " + e.getMessage());
        }

        return dataList;
    }
}
