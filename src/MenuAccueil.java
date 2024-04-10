import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenêtre de menu d'accueil après la connexion utilisateur.
 * Affiche un menu avec des options pour les actions utilisateur.
 */
public class MenuAccueil extends JFrame {
    private User user;

    /**
     * Constructeur pour créer la fenêtre de menu d'accueil.
     * @param user Utilisateur connecté
     */
    public MenuAccueil(User user) {
        this.user = user;

        setTitle("Menu d'accueil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = createHeaderPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        
        // Ajuster la taille de la fenêtre en fonction du contenu
        pack();
        // Centrer la fenêtre sur l'écran
        setLocationRelativeTo(null); // Centrer la fenêtre
        // Rendre la fenêtre visible
        setVisible(true);
    }

    /**
     * Crée le panneau d'en-tête affichant un message de bienvenue avec le prénom de l'utilisateur.
     * @return JPanel Panneau d'en-tête
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Bienvenue, " + this.user.getPrenom() + "!");
        headerPanel.add(welcomeLabel);
        return headerPanel;
    }

    /**
     * Crée le panneau de boutons pour les options du menu.
     * @return JPanel Panneau de boutons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // 5 lignes, 1 colonne
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Ajoute des marges

        JButton empruntsButton = createButton("Mes Emprunts");
        JButton livresButton = createButton("Catalogue");
        JButton retardsButton = createButton("Mes Retards");
        JButton gestionButton = createButton("Gestion Membre");
        JButton deconnexionButton = createButton("Déconnexion");

        // Vérifier le rôle de l'utilisateur avant de créer le bouton "Gestion Membre"
        if (user.getRole() == 1) {
            buttonPanel.add(gestionButton);
        }

        buttonPanel.add(empruntsButton);
        buttonPanel.add(livresButton);
        buttonPanel.add(retardsButton);
        buttonPanel.add(deconnexionButton);

        return buttonPanel;
    }

    /**
     * Crée un bouton avec l'étiquette spécifiée et ajoute un écouteur d'événements.
     * @param label Étiquette du bouton
     * @return JButton Bouton créé
     */
    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            /**
             * Méthode exécutée lorsqu'un bouton est cliqué.
             * @param e Événement de clic
             */
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(label);
            }
        });
        return button;
    }

    /**
     * Gère les actions à effectuer lorsque le bouton est cliqué en fonction de l'étiquette du bouton.
     * @param buttonLabel Étiquette du bouton cliqué
     */
    private void handleButtonClick(String buttonLabel) {
        switch (buttonLabel) {
            case "Mes Emprunts":
                ListeEmprunt empruntPage = new ListeEmprunt(user);
                empruntPage.setVisible(true);
                dispose();
                break;
            case "Catalogue":
                LibraryManagementApp catalogue = new LibraryManagementApp(user);
                catalogue.setVisible(true);
                dispose();
                break;
            case "Mes Retards":
                ListePenaliter retard = new ListePenaliter(user);
                retard.setVisible(true);
                dispose();
                break;
            case "Gestion Membre":
                GestionMembre membre = new GestionMembre(user);
                membre.setVisible(true);
                dispose();
                break;
            case "Déconnexion":
            	LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
                break;
            default:
                break;
        }
    }
}
