import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Représente une notation d'un livre par un utilisateur avec une note et un commentaire.
 */
public class Noter {
    private String ISBN;
    private int IdUser;
    private int note;
    private String commentaire;

    /**
     * Constructeur pour créer un objet Noter avec les détails spécifiés.
     * @param ISBN ISBN du livre noté
     * @param user Utilisateur qui note le livre
     * @param note Note attribuée au livre
     * @param commentaire Commentaire associé à la note
     */
    public Noter(String ISBN, User user, int note, String commentaire) {
        this.ISBN = ISBN;
        this.IdUser = user.getIdUser();
        this.note = note;
        this.commentaire = commentaire;
    }

    /**
     * Obtient l'ISBN du livre noté.
     * @return ISBN du livre
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Définit l'ISBN du livre noté.
     * @param ISBN ISBN à définir
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Obtient l'identifiant de l'utilisateur qui note le livre.
     * @return Identifiant de l'utilisateur
     */
    public int getIdUser() {
        return IdUser;
    }

    /**
     * Définit l'identifiant de l'utilisateur qui note le livre.
     * @param idUser Identifiant de l'utilisateur à définir
     */
    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    /**
     * Obtient la note attribuée au livre.
     * @return Note attribuée
     */
    public int getNote() {
        return note;
    }

    /**
     * Définit la note attribuée au livre.
     * @param note Note à définir
     */
    public void setNote(int note) {
        this.note = note;
    }

    /**
     * Obtient le commentaire associé à la note du livre.
     * @return Commentaire associé à la note
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit le commentaire associé à la note du livre.
     * @param commentaire Commentaire à définir
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * Représentation textuelle de l'objet Noter.
     * @return Chaîne représentant l'objet Noter
     */
    @Override
    public String toString() {
        return "Noter [ISBN=" + ISBN + ", IdUser=" + IdUser + ", note=" + note + ", commentaire=" + commentaire + "]";
    }

    /**
     * Récupère les informations de notation d'un livre depuis la base de données.
     * @param ISBN ISBN du livre noté
     * @param idUser Identifiant de l'utilisateur
     * @param connection Connexion à la base de données
     * @return ResultSet contenant les informations de notation si elles existent, sinon null
     */
    private ResultSet getNoter(String ISBN, int idUser, Connection connection) {
        try {
            String query = "SELECT * FROM noter WHERE ISBN = ? AND idUser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ISBN);
            statement.setInt(2, idUser);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insère une nouvelle note dans la base de données.
     * @param connection Connexion à la base de données
     * @return true si l'insertion est réussie, sinon false
     */
    private boolean insertNote(Connection connection) {
        try {
            String query = "INSERT INTO noter (ISBN, idUser, note, commentaire) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.getISBN());
            statement.setInt(2, this.getIdUser());
            statement.setInt(3, this.getNote());
            statement.setString(4, this.getCommentaire());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour une note existante dans la base de données.
     * @param connection Connexion à la base de données
     * @return true si la mise à jour est réussie, sinon false
     */
    private boolean updateNote(Connection connection) {
        try {
            String query = "UPDATE noter SET note = ?, commentaire = ? WHERE ISBN = ? AND idUser = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.getNote());
            statement.setString(2, this.getCommentaire());
            statement.setString(3, this.getISBN());
            statement.setInt(4, this.getIdUser());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Enregistre une nouvelle notation pour un livre dans la base de données.
     * @return true si la notation est enregistrée avec succès, sinon false
     */
    public boolean noterUnLivre() {
        Connection connection = null;
        boolean res = false;
        try {
            connection = Database.getConnection();
            ResultSet resultSet = this.getNoter(this.ISBN, this.getIdUser(), connection);
            if (resultSet == null && this.insertNote(connection)) {
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }
        return res;
    }

    /**
     * Modifie une notation existante pour un livre dans la base de données.
     * @return true si la modification est effectuée avec succès, sinon false
     */
    public boolean modifierUneNote() {
        Connection connection = null;
        boolean res = false;
        try {
            connection = Database.getConnection();
            ResultSet resultSet = this.getNoter(this.ISBN, this.getIdUser(), connection);
            if (resultSet != null && this.updateNote(connection)) {
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connection);
        }
        return res;
    }
}
