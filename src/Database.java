import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
    // Informations de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost/projet_bibliotheque";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Méthode pour établir la connexion à la base de données
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Chargement du pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établissement de la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie à la base de données !");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
        return connection;
    }

    // Méthode pour fermer la connexion à la base de données
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée !");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    
    // Méthode pour fermer les ressources
    public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
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
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM " + tableName;
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

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
        } finally {
            // Fermeture des ressources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeConnection(connection);
        }

        return dataList;
    }
    
    public static User verifyUserCredentials(String email, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Utilisation du constructeur avec ResultSet pour créer un objet User
                return new User(resultSet);
            } else {
                // Si aucun utilisateur correspondant n'est trouvé, retourner null
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification des informations de l'utilisateur : " + e.getMessage());
            return null;
        } finally {
            Database.closeResources(connection, statement, resultSet);
        }
    }
}


