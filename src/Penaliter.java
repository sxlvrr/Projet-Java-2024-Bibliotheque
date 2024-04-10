import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une pénalité associée à un utilisateur pour un livre.
 */
public class Penaliter {
    private String ISBN;
    private int IdUser;
    private LocalDate datePenalite;

    /**
     * Constructeur pour créer une pénalité pour un utilisateur associé à un livre.
     * La date de pénalité est initialisée à la date actuelle.
     * @param ISBN ISBN du livre associé à la pénalité
     * @param user Utilisateur associé à la pénalité
     */
    public Penaliter(String ISBN, User user) {
        this.ISBN = ISBN;
        this.IdUser = user.getIdUser();
        this.datePenalite = LocalDate.now();
    }
    
    /**
     * Constructeur pour créer une pénalité pour un utilisateur associé à un livre.
     * La date de pénalité est initialisée à la date actuelle.
     * @param ISBN ISBN du livre associé à la pénalité
     * @param user Utilisateur associé à la pénalité
     */
    public Penaliter(String ISBN, User user, LocalDate datePenalite) {
        this.ISBN = ISBN;
        this.IdUser = user.getIdUser();
        this.datePenalite = datePenalite;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int IdUser) {
        this.IdUser = IdUser;
    }

    public LocalDate getDatePenalite() {
        return datePenalite;
    }

    public void setDatePenalite(LocalDate datePenalite) {
        this.datePenalite = datePenalite;
    }

    @Override
    public String toString() {
        return "Penaliter [ISBN=" + ISBN + ", IdUser=" + IdUser + ", datePenalite=" + datePenalite + "]";
    }

    /**
     * Vérifie s'il existe déjà une pénalité pour un emprunt actuel.
     * @param emprunt Emprunt associé à la pénalité
     * @param connection Connexion à la base de données
     * @return true s'il existe une pénalité, false sinon
     */
    public static Boolean checkPenaliter(Emprunter emprunt, Connection connection) {
        try {
            String query = "SELECT * FROM penaliter WHERE ISBN = ? AND idUser = ? AND datePenalite <= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, emprunt.getISBN());
            statement.setString(2, String.valueOf(emprunt.getIdUser()));
            statement.setString(3, String.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true; // Il existe une pénalité pour cet emprunt
            } else {
                return false; // Il n'existe pas de pénalité pour cet emprunt
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Échec de la requête SQL, considérer comme ayant une pénalité
        }
    }

    /**
     * Récupère la liste des pénalités associées à un utilisateur.
     * @param user Utilisateur dont on veut récupérer les pénalités
     * @return Liste des pénalités de l'utilisateur
     */
    public static List<Penaliter> mesPenaliter(User user) {
        List<Penaliter> listPenaliter = new ArrayList<>();
        Connection connection = null;
        try {
            connection = Database.getConnection();
            String query = "SELECT * FROM penaliter WHERE idUser = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getIdUser());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                LocalDate datePenalite = LocalDate.parse(resultSet.getString("datePenalite"));

                Penaliter penaliter = new Penaliter(ISBN, user);
                penaliter.setDatePenalite(datePenalite);

                listPenaliter.add(penaliter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Database.closeConnection(connection);
        return listPenaliter;
    }
    
    /**
     * Supprime toutes les pénalités associées à un utilisateur par son adresse e-mail.
     * @param email Adresse e-mail de l'utilisateur dont les pénalités doivent être supprimées
     * @return true si les pénalités ont été supprimées avec succès, sinon false
     */
    public static boolean deletePenalitesByEmail(String email) {
        Connection connection = null;
        try {
            connection = Database.getConnection();
            String query = "DELETE FROM penaliter WHERE idUser = (SELECT idUser FROM users WHERE email = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            int rowsAffected = statement.executeUpdate();

            // Vérifier si des lignes ont été supprimées
            if (rowsAffected > 0) {
                return true; // Suppression réussie
            } else {
                return false; // Aucune pénalité trouvée pour cet utilisateur
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Erreur lors de la suppression
        } finally {
            Database.closeConnection(connection);
        }
    }
    
    /**
     * Insère toutes les nouvelles pénalités détectées pour les emprunts en retard.
     * Cette méthode appelle Emprunter.rechercherEmpruntsEnRetard() pour obtenir les nouveaux emprunts en retard
     * et insère des pénalités pour ces emprunts si nécessaire.
     * @return Le nombre de nouvelles pénalités insérées
     */
    public static int insererNouvellesPenalites() {
        int nouvellesPenalitesInseres = 0;
        Connection connection = null;
        try {
            connection = Database.getConnection();
            List<Penaliter> nouvellesPenalites = Emprunter.rechercherEmpruntsEnRetard();

            for (Penaliter penalite : nouvellesPenalites) {
                if (insertPenalite(penalite, connection)) {
                    nouvellesPenalitesInseres++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }
        return nouvellesPenalitesInseres;
    }

    /**
     * Insère une nouvelle pénalité dans la base de données.
     * @param penalite La pénalité à insérer
     * @param connection La connexion à la base de données
     * @return true si l'insertion a réussi, sinon false
     */
    private static boolean insertPenalite(Penaliter penalite, Connection connection) {
        try {
            String query = "INSERT INTO penaliter (ISBN, idUser, datePenalite) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, penalite.getISBN());
            statement.setInt(2, penalite.getIdUser());
            statement.setString(3, String.valueOf(penalite.getDatePenalite()));
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
