import java.util.Date;

public class Utilisateur extends Database{
    private int idUser;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private int role;
    private Date createdDate;
    private Date updatedDate;

    // Constructeur
    public Utilisateur(int idUser, String nom, String prenom, String email, String password, int role, Date createdDate, Date updatedDate) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    
    // Getters et setters
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "Utilisateur [idUser=" + idUser + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email
				+ ", password=" + password + ", role=" + role + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + "]";
	}
	
    
}
