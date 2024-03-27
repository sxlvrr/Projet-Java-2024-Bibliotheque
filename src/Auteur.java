import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Auteur {
    private int idAuteur;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String nationalite;

    // Constructeur
    public Auteur(int idAuteur, String nom, String prenom, Date dateNaissance, String nationalite) {
        this.idAuteur = idAuteur;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.nationalite = nationalite;
    }
    
    // Getters et setters
	public int getIdAuteur() {
		return idAuteur;
	}

	public void setIdAuteur(int idAuteur) {
		this.idAuteur = idAuteur;
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

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getNationalite() {
		return nationalite;
	}

	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}

	@Override
	public String toString() {
		return "Auteur [idAuteur=" + idAuteur + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance="
				+ dateNaissance + ", nationalite=" + nationalite + "]";
	}
	
	// Méthode pour récupérer les informations de l'auteur à partir de son ID
    public static Auteur fetchAuthorById(int idAuteur, Connection connection) {
        Auteur auteur = null;

        try {
            // Préparation de la requête SQL pour récupérer les informations de l'auteur
            String query = "SELECT * FROM auteur WHERE idAuteur = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idAuteur);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Vérification si l'auteur existe
            if (resultSet.next()) {
                // Récupération des données de l'auteur depuis le ResultSet
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                Date dateNaissance = resultSet.getDate("dateNaissance");
                String nationalite = resultSet.getString("nationalite");

                // Création de l'objet Auteur correspondant
                auteur = new Auteur(idAuteur, nom, prenom, dateNaissance, nationalite);
            }

            // Fermeture des ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return auteur;
    }

    
}
