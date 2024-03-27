import java.util.Date;

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

    
}
