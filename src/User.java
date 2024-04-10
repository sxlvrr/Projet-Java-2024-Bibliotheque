import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Représente un utilisateur dans le système.
 */
public class User {
    private int idUser;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private int role;
    private Date createdDate;
    private Date updatedDate;

    /**
     * Constructeur par défaut.
     */
    public User() {
    }

    /**
     * Constructeur prenant un ResultSet comme paramètre pour initialiser un utilisateur.
     * @param resultSet Résultat de la requête SQL
     * @throws SQLException En cas d'erreur SQL lors de la récupération des données
     */
    public User(ResultSet resultSet) throws SQLException {
        this.idUser = resultSet.getInt("idUser");
        this.nom = resultSet.getString("nom");
        this.prenom = resultSet.getString("prenom");
        this.email = resultSet.getString("email");
        this.password = resultSet.getString("password");
        this.role = resultSet.getInt("role");
    }
    
    /**
     * Constructeur prenant des paramètres pour initialiser un utilisateur.
     * @param idUser
     * @param nom
     * @param prenom
     * @param email
     * @param role
     */
    public User(Integer idUser, String nom, String prenom, String email, Integer role){
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }
    
    /**
     * Constructeur prenant que idUser en paramètre pour initialiser un utilisateur.
     * @param idUser
     */
    public User(Integer idUser){
        this.idUser = idUser;
    }

    // Getters et setters pour les attributs de l'utilisateur

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    /**
     * Méthode statique pour chiffrer un mot de passe avec BCrypt.
     * @param password Mot de passe en clair
     * @return Mot de passe chiffré
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Méthode statique pour vérifier si un mot de passe en clair correspond à un mot de passe chiffré.
     * @param password Mot de passe en clair
     * @param hashPassword Mot de passe chiffré
     * @return true si le mot de passe correspond, sinon false
     */
    public static Boolean checkHashPassword(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }

    /**
     * Méthode statique pour vérifier un utilisateur avec un email et un mot de passe.
     * @param email Email de l'utilisateur
     * @param password Mot de passe en clair
     * @return Objet User correspondant à l'email et au mot de passe fournis, ou null si non trouvé
     */
    public static User verifyPassword(String email, String password) {
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("password");
                return (checkHashPassword(password, hashedPasswordFromDB)) ? new User(resultSet) : null;
            } else {
                return null; // L'email n'existe pas dans la base de données
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null; // Une erreur s'est produite lors de la vérification du mot de passe
        }
    }

    /**
     * Méthode pour vérifier si l'utilisateur a un certain rôle.
     * @param role Role à vérifier
     * @return true si l'utilisateur a le rôle spécifié, sinon false
     */
    public boolean hasRole(int role) {
        return this.role == role;
    }

    /**
     * Méthode statique pour créer un nouvel utilisateur dans la base de données.
     * @param firstName Prénom de l'utilisateur
     * @param lastName Nom de l'utilisateur
     * @param email Email de l'utilisateur
     * @param password Mot de passe en clair
     * @return true si l'utilisateur est créé avec succès, sinon false
     */
    public static boolean createUser(String firstName, String lastName, String email, String password) {
        // Chiffrer le mot de passe
        String hashedPassword = hashPassword(password);

        // Insérer l'utilisateur dans la base de données
        try (Connection connection = Database.getConnection()) {
            String query = "INSERT INTO users (nom, prenom, email, password, role) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lastName);
            statement.setString(2, firstName);
            statement.setString(3, email);
            statement.setString(4, hashedPassword);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Méthode statique pour mettre à jour le rôle d'un utilisateur dans la base de données.
     * @param email Email de l'utilisateur à mettre à jour
     * @param newRole Nouveau rôle à assigner (0 ou 1)
     * @return true si la mise à jour est réussie, sinon false
     */
    public static boolean updateUserRole(String email, int newRole) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();
            String query = "UPDATE users SET role = ? WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, newRole);
            statement.setString(2, email);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Fermer les ressources JDBC
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Supprime un utilisateur de la base de données en fonction de l'e-mail.
     * @param email E-mail de l'utilisateur à supprimer
     * @return true si l'utilisateur est supprimé avec succès, sinon false
     */
    public static boolean deleteUser(String email) {
        String query = "DELETE FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Récupère tout les utilisateurs
     * @param connection
     * @return
     * @throws SQLException
     */
    public static List<User> getAllUsers(Connection connection) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idUser = resultSet.getInt("idUser");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");
                int role = resultSet.getInt("role");

                // Création d'un nouvel objet User et ajout à la liste
                User user = new User(idUser, nom, prenom, email, role);
                users.add(user);
            }
        }

        return users;
    }

}
