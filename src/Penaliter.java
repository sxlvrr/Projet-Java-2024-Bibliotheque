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
}
