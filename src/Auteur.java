import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Représente un auteur avec ses attributs tels que ID, nom, prénom, date de naissance et nationalité.
 */
public class Auteur {
    private int idAuteur;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String nationalite;

    /**
     * Constructeur pour créer un objet Auteur avec les détails spécifiés.
     * @param idAuteur     ID de l'auteur
     * @param nom          Nom de l'auteur
     * @param prenom       Prénom de l'auteur
     * @param dateNaissance Date de naissance de l'auteur
     * @param nationalite  Nationalité de l'auteur
     */
    public Auteur(int idAuteur, String nom, String prenom, LocalDate dateNaissance, String nationalite) {
        this.idAuteur = idAuteur;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.nationalite = nationalite;
    }

    /**
     * Obtient l'ID de l'auteur.
     * @return ID de l'auteur
     */
    public int getIdAuteur() {
        return idAuteur;
    }

    /**
     * Définit l'ID de l'auteur.
     * @param idAuteur ID à définir
     */
    public void setIdAuteur(int idAuteur) {
        this.idAuteur = idAuteur;
    }

    /**
     * Obtient le nom de l'auteur.
     * @return Nom de l'auteur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'auteur.
     * @param nom Nom à définir
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient le prénom de l'auteur.
     * @return Prénom de l'auteur
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom de l'auteur.
     * @param prenom Prénom à définir
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Obtient la date de naissance de l'auteur.
     * @return Date de naissance de l'auteur
     */
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Définit la date de naissance de l'auteur.
     * @param dateNaissance Date de naissance à définir
     */
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Obtient la nationalité de l'auteur.
     * @return Nationalité de l'auteur
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * Définit la nationalité de l'auteur.
     * @param nationalite Nationalité à définir
     */
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    /**
     * Convertit l'objet Auteur en une représentation sous forme de chaîne de caractères.
     * @return Représentation textuelle de l'objet Auteur
     */
    @Override
    public String toString() {
        return "Auteur [idAuteur=" + idAuteur + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance="
                + dateNaissance + ", nationalite=" + nationalite + "]";
    }

    /**
     * Récupère les informations d'un auteur à partir de son ID dans la base de données.
     * @param idAuteur   ID de l'auteur à récupérer
     * @param connection Connexion à la base de données
     * @return Objet Auteur correspondant à l'ID spécifié, ou null si aucun auteur correspondant n'est trouvé
     * @throws SQLException Si une erreur SQL survient lors de l'exécution de la requête
     */
    public static Auteur fetchAuthorById(int idAuteur, Connection connection) throws SQLException {
        Auteur auteur = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Préparation de la requête SQL pour récupérer les informations de l'auteur
            String query = "SELECT * FROM auteur WHERE idAuteur = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idAuteur);

            // Exécution de la requête
            resultSet = statement.executeQuery();

            // Vérification si l'auteur existe
            if (resultSet.next()) {
                // Récupération des données de l'auteur depuis le ResultSet
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                LocalDate dateNaissance = resultSet.getDate("dateNaissance").toLocalDate();
                String nationalite = resultSet.getString("nationalite");

                // Création de l'objet Auteur correspondant
                auteur = new Auteur(idAuteur, nom, prenom, dateNaissance, nationalite);
            }
        } finally {
            // Fermeture des ressources dans un bloc finally pour garantir leur fermeture
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return auteur;
    }
}
