import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant les emprunts de livres effectués par un utilisateur.
 */
public class Emprunter {
    private String ISBN;
    private int IdUser;
    private LocalDate dateEmprunt;
    private LocalDate dateRetour;

    /**
     * Constructeur pour créer un nouvel emprunt.
     * @param ISBN ISBN du livre emprunté
     * @param user Utilisateur effectuant l'emprunt
     */
    public Emprunter(String ISBN, User user) {
        this.ISBN = ISBN;
        this.IdUser = user.getIdUser();
        this.dateEmprunt = LocalDate.now();
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    @Override
    public String toString() {
        return "Emprunter [ISBN=" + ISBN + ", IdUser=" + IdUser + ", dateEmprunt=" + dateEmprunt + ", dateRetour="
                + dateRetour + "]";
    }

    /**
     * Méthode pour rendre un livre emprunté.
     * @return Boolean indiquant si le livre a été rendu avec succès
     */
    public Boolean rendreUnLivre() {
        Boolean res = false;
        Connection connection = null;
        try {
            connection = Database.getConnection();
            if (this.checkDejaEmprunter(connection)) {
                String query = "UPDATE emprunter SET dateRetour = ? WHERE ISBN = ? AND idUser = ? AND dateRetour IS NULL";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, String.valueOf(LocalDate.now()));
                statement.setString(2, this.getISBN());
                statement.setInt(3, this.getIdUser());
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    Stock.updateStock(this.getISBN(), true, connection);
                    res = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }
        return res;
    }

    /**
     * Méthode pour effectuer un nouvel emprunt de livre.
     * @return Boolean indiquant si l'emprunt a été effectué avec succès
     */
    public Boolean emprunterUnLivre() {
        Boolean res = false;
        Connection connection = null;
        try {
            connection = Database.getConnection();
            if (!this.checkDejaEmprunter(connection) && Stock.checkStock(this.getISBN(), connection)) {
                if (createEmprunter(connection)) {
                    Stock.updateStock(this.getISBN(), false, connection);
                    res = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }
        return res;
    }

    /**
     * Méthode pour vérifier si un livre est déjà emprunté par cet utilisateur.
     * @param connection Connexion à la base de données
     * @return Boolean indiquant si le livre est déjà emprunté
     */
    private Boolean checkDejaEmprunter(Connection connection) {
        try {
            String query = "SELECT * FROM emprunter WHERE ISBN = ? AND idUser = ? AND dateRetour IS NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.getISBN());
            statement.setInt(2, this.getIdUser());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true; // Il y a déjà un emprunt en cours pour ce livre par cet utilisateur
            } else {
                return Penaliter.checkPenaliter(this, connection); // Vérifie s'il y a une pénalité de retour pour cet utilisateur
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Échec de la requête, considérer comme déjà emprunté pour éviter de créer un nouvel enregistrement
        }
    }

    /**
     * Méthode pour créer un nouvel emprunt dans la base de données.
     * @param connection Connexion à la base de données
     * @return Boolean indiquant si la création de l'emprunt a réussi
     */
    private Boolean createEmprunter(Connection connection) {
        try {
            String query = "INSERT INTO emprunter (ISBN, idUser, dateEmprunt) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.getISBN());
            statement.setInt(2, this.getIdUser());
            statement.setString(3, String.valueOf(this.getDateEmprunt()));
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Méthode pour récupérer la liste des emprunts d'un utilisateur.
     * @param user Utilisateur pour lequel on souhaite récupérer les emprunts
     * @return Liste des emprunts de l'utilisateur
     */
    public static List<Emprunter> mesEmprunts(User user) {
        List<Emprunter> listEmprunt = new ArrayList<>();
        Connection connection = null;
        try {
            connection = Database.getConnection();
            String query = "SELECT * FROM emprunter WHERE idUser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getIdUser());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                LocalDate dateEmprunt = LocalDate.parse(resultSet.getString("dateEmprunt"));
                LocalDate dateRetour = (resultSet.getString("dateRetour") != null) ? LocalDate.parse(resultSet.getString("dateRetour")) : null;

                Emprunter emprunt = new Emprunter(ISBN, user);
                emprunt.setDateEmprunt(dateEmprunt);
                emprunt.setDateRetour(dateRetour);

                listEmprunt.add(emprunt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }
        return listEmprunt;
    }
}
