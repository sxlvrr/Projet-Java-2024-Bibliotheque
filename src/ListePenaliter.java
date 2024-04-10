import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Fenêtre affichant la liste des pénalités pour un utilisateur.
 */
public class ListePenaliter extends JFrame {

    private JTable table;
    private JButton retourButton;
    private User user;
    private List<Livre> livres = new ArrayList<>();

    /**
     * Constructeur de la classe ListePenaliter.
     * @param user L'utilisateur pour lequel afficher les pénalités.
     */
    public ListePenaliter(User user) {
        this.user = user;
        Penaliter.insererNouvellesPenalites();
        setTitle("Liste des Pénalités");
        setSize(800, 400); // Ajuster la largeur pour inclure le titre du livre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setupActions();
        setLocationAndDisplay();
    }

    /**
     * Initialisation des composants de l'interface graphique.
     */
    private void initComponents() {
        // Création du modèle de table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ISBN");
        model.addColumn("Titre");
        model.addColumn("Date de Pénalité");
        
        // Récupération des livres associés à la base de données
        Connection connection = null;
		try {
			connection = Database.getConnection();
			this.livres = Livre.fetchBooksFromDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
		Database.closeConnection(connection);
        
        // Récupération des pénalités associées à l'utilisateur
        List<Penaliter> penalites = Penaliter.mesPenaliter(user);

        // Remplissage du modèle avec les données de pénalité
        for (Penaliter penalite : penalites) {
        	String ISBN = penalite.getISBN();
            String titre = "";
            String datePenalite = penalite.getDatePenalite().toString();
            
        	for (Livre livre : livres) {
                if (livre.getISBN().equals(ISBN)) {
                    titre = (livre != null) ? livre.getTitre() : "Titre inconnu";
                }
            }            
            model.addRow(new Object[]{ISBN, titre, datePenalite});
        }

        // Création de la table avec le modèle
        table = new JTable(model);
        table.setRowHeight(25); // Hauteur des lignes

        // Création du bouton Retour
        retourButton = new JButton("Retour");
        retourButton.setFocusPainted(false); // Supprime l'effet de focus

        // Stylisation de l'en-tête de la table
        table.getTableHeader().setBackground(Color.LIGHT_GRAY); // Couleur de l'en-tête
    }

    /**
     * Configuration des actions des composants.
     */
    private void setupActions() {
        // Action du bouton Retour
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retour au menu principal (MenuAccueil)
                MenuAccueil menuAccueil = new MenuAccueil(user);
                menuAccueil.setVisible(true);
                dispose(); // Ferme la fenêtre actuelle (ListePenaliter)
            }
        });
    }

    /**
     * Positionnement des composants dans la fenêtre et affichage.
     */
    private void setLocationAndDisplay() {
        // Création du panneau pour le bouton Retour et positionnement en haut à gauche
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Espacement du panneau
        buttonPanel.add(retourButton);

        // Création du panneau pour le titre du tableau
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder("Liste des Pénalités"));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajout des panneaux au contenu de la JFrame
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(titlePanel, BorderLayout.CENTER);

        // Ajuster la taille de la fenêtre en fonction du contenu
        pack();
        // Centrer la fenêtre sur l'écran
        setLocationRelativeTo(null);
        // Rendre la fenêtre visible
        setVisible(true);
    }
}
