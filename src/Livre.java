import java.util.Date;

public class Livre {
    private String ISBN;
    private String titre;
    private String editeur;
    private String genre;
    private int nbPage;
    private String langue;
    private Date datePublication;
    private String description;
    private int idAuteur;
    private int idStock;

    // Constructeur
    public Livre(String ISBN, String titre, String editeur, String genre, int nbPage, String langue, Date datePublication, String description, int idAuteur, int idStock) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.editeur = editeur;
        this.genre = genre;
        this.nbPage = nbPage;
        this.langue = langue;
        this.datePublication = datePublication;
        this.description = description;
        this.idAuteur = idAuteur;
        this.idStock = idStock;
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

	public int getIdAuteur() {
		return idAuteur;
	}

	public void setIdAuteur(int idAuteur) {
		this.idAuteur = idAuteur;
	}

	public int getIdStock() {
		return idStock;
	}

	public void setIdStock(int idStock) {
		this.idStock = idStock;
	}

	@Override
	public String toString() {
		return "Livre [ISBN=" + ISBN + ", titre=" + titre + ", editeur=" + editeur + ", genre=" + genre + ", nbPage="
				+ nbPage + ", langue=" + langue + ", datePublication=" + datePublication + ", description="
				+ description + ", idAuteur=" + idAuteur + ", idStock=" + idStock + "]";
	}
    
	
}
