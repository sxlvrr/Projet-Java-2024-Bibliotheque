import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Livre {
    private String ISBN;
    private String titre;
    private String editeur;
    private String genre;
    private int nbPage;
    private String langue;
    private Date datePublication;
    private String description;
    private Auteur auteur;
    private Stock stock;

    // Constructeur
    public Livre(String ISBN, String titre, String editeur, String genre, int nbPage, String langue, Date datePublication, String description, Auteur auteur, Stock stock) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.editeur = editeur;
        this.genre = genre;
        this.nbPage = nbPage;
        this.langue = langue;
        this.datePublication = datePublication;
        this.description = description;
        this.auteur = auteur;
        this.stock = stock;
    }
    
    // Getters et setters
	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getEditeur() {
		return editeur;
	}

	public void setEditeur(String editeur) {
		this.editeur = editeur;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getNbPage() {
		return nbPage;
	}

	public void setNbPage(int nbPage) {
		this.nbPage = nbPage;
	}

	public String getLangue() {
		return langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}

	public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Auteur getAuteur() {
		return auteur;
	}

	public void setAuteur(Auteur auteur) {
		this.auteur = auteur;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "\nLivre [ISBN=" + ISBN + ", titre=" + titre + ", editeur=" + editeur + ", genre=" + genre + ", nbPage="
				+ nbPage + ", langue=" + langue + ", datePublication=" + datePublication + ", description="
				+ description + ", \n    Auteur=" + auteur.toString() + "]";
	}
    
	
	public static List<Livre> fetchBooksFromDatabase(Connection connection) throws SQLException {
        List<Livre> livres = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM livre");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Récupérer les données du livre depuis le ResultSet
                String ISBN = resultSet.getString("ISBN");
                String titre = resultSet.getString("titre");
                String editeur = resultSet.getString("editeur");
                String genre = resultSet.getString("genre");
                int nbPage = resultSet.getInt("nbPage");
                String langue = resultSet.getString("langue");
                Date datePublication = resultSet.getDate("datePublication");
                String description = resultSet.getString("description");
                int idAuteur = resultSet.getInt("idAuteur");
                int idStock = resultSet.getInt("idStock");

                Auteur auteur = Auteur.fetchAuthorById(idAuteur, connection);
                Stock stock = Stock.fetchStockById(idStock, connection);

                // Créer l'objet Livre
                Livre livre = new Livre(ISBN, titre, editeur, genre, nbPage, langue, datePublication, description, auteur, stock);

                // Ajouter le livre à la liste
                livres.add(livre);
            }
        }

        return livres;
    }
	
	// Méthode pour récupérer un livre par son ISBN
    public static Livre getLivreByISBN(String ISBN) {
        // Exemple d'une liste en mémoire de livres
        List<Livre> livres = new ArrayList<>();
        

        // Parcourir la liste des livres pour trouver le livre par son ISBN
        for (Livre livre : livres) {
            if (livre.getISBN().equals(ISBN)) {
                return livre;
            }
        }

        // Retourner null si aucun livre avec cet ISBN n'est trouvé
        return null;
    }
}
