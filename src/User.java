import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private int idUser;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private int role;
    private Date createdDate;
    private Date updatedDate;

    public User() {
    }

    public User(ResultSet resultSet) throws SQLException {
        this.idUser = resultSet.getInt("idUser");
        this.nom = resultSet.getString("nom");
        this.prenom = resultSet.getString("prenom");
        this.email = resultSet.getString("email");
        this.password = resultSet.getString("password");
        this.role = resultSet.getInt("role");
    }

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

    // Méthode pour chiffrer le mot de passe
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    // Méthode pour tester le mot de passe crypté avec le mot de passe clair
    public static Boolean checkHashPassword(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }
    
    // Méthode pour vérifier le mot de passe
    public static User verifyPassword(String email, String password) {
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
            	//Verif password hash
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

    // Méthode pour vérifier si l'utilisateur a un certain rôle
    public boolean hasRole(int role) {
        return this.role == role;
    }
    
    // Méthode pour créer un nouvel utilisateur dans la base de données
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
}
