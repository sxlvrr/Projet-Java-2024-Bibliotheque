import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente un livre avec ses attributs tels que ISBN, titre, éditeur, etc.
 */
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

    /**
     * Constructeur pour créer un objet Livre avec les détails spécifiés.
     * 
     * @param ISBN             ISBN du livre
     * @param titre            Titre du livre
     * @param editeur          Éditeur du livre
     * @param genre            Genre du livre
     * @param nbPage           Nombre de pages du livre
     * @param langue           Langue du livre
     * @param datePublication  Date de publication du livre
     * @param description      Description du livre
     * @param auteur           Auteur du livre
     * @param stock            Stock du livre
     */
    public Livre(String ISBN, String titre, String editeur, String genre, int nbPage, String langue, Date datePublication,
                 String description, Auteur auteur, Stock stock) {
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

    /**
     * Obtient l'ISBN du livre.
     * 
     * @return ISBN du livre
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Définit l'ISBN du livre.
     * 
     * @param ISBN ISBN à définir
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Obtient le titre du livre.
     * 
     * @return Titre du livre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre du livre.
     * 
     * @param titre Titre à définir
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Obtient l'éditeur du livre.
     * 
     * @return Éditeur du livre
     */
    public String getEditeur() {
        return editeur;
    }

    /**
     * Définit l'éditeur du livre.
     * 
     * @param editeur Éditeur à définir
     */
    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    /**
     * Obtient le genre du livre.
     * 
     * @return Genre du livre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Définit le genre du livre.
     * 
     * @param genre Genre à définir
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Obtient le nombre de pages du livre.
     * 
     * @return Nombre de pages du livre
     */
    public int getNbPage() {
        return nbPage;
    }

    /**
     * Définit le nombre de pages du livre.
     * 
     * @param nbPage Nombre de pages à définir
     */
    public void setNbPage(int nbPage) {
        this.nbPage = nbPage;
    }

    /**
     * Obtient la langue du livre.
     * 
     * @return Langue du livre
     */
    public String getLangue() {
        return langue;
    }

    /**
     * Définit la langue du livre.
     * 
     * @param langue Langue à définir
     */
    public void setLangue(String langue) {
        this.langue = langue;
    }

    /**
     * Obtient la date de publication du livre.
     * 
     * @return Date de publication du livre
     */
    public Date getDatePublication() {
        return datePublication;
    }

    /**
     * Définit la date de publication du livre.
     * 
     * @param datePublication Date de publication à définir
     */
    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    /**
     * Obtient la description du livre.
     * 
     * @return Description du livre
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description du livre.
     * 
     * @param description Description à définir
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtient l'auteur du livre.
     * 
     * @return Auteur du livre
     */
    public Auteur getAuteur() {
        return auteur;
    }

    /**
     * Définit l'auteur du livre.
     * 
     * @param auteur Auteur à définir
     */
    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    /**
     * Obtient le stock du livre.
     * 
     * @return Stock du livre
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Définit le stock du livre.
     * 
     * @param stock Stock à définir
     */
    public void setStock(Stock stock) {
        this.stock = stock;
    }

    /**
     * Récupère la liste des livres depuis la base de données.
     * 
     * @param connection Connexion à la base de données
     * @return Liste des livres récupérés depuis la base de données
     * @throws SQLException Si une erreur SQL survient lors de l'exécution de la requête
     */
    public static List<Livre> fetchBooksFromDatabase(Connection connection) throws SQLException {
        List<Livre> livres = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM livre");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Récupération des données du livre depuis le ResultSet
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

                // Récupération de l'auteur et du stock associés au livre
                Auteur auteur = Auteur.fetchAuthorById(idAuteur, connection);
                Stock stock = Stock.fetchStockById(idStock, connection);

                // Création de l'objet Livre
                Livre livre = new Livre(ISBN, titre, editeur, genre, nbPage, langue, datePublication, description, auteur, stock);

                // Ajout du livre à la liste
                livres.add(livre);
            }
        }

        return livres;
    }

    /**
     * Récupère un livre par son ISBN à partir d'une liste en mémoire.
     * 
     * @param ISBN   ISBN du livre à rechercher
     * @param livres Liste des livres en mémoire
     * @return Le livre correspondant à l'ISBN spécifié, ou null si aucun livre correspondant n'est trouvé
     */
    public static Livre getLivreByISBN(String ISBN, List<Livre> livres) {
        for (Livre livre : livres) {
            if (livre.getISBN().equals(ISBN)) {
                return livre;
            }
        }
        return null;
    }
}
