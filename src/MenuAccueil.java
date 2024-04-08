import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuAccueil extends JFrame {
    private User user;

    public MenuAccueil(User user) {
        this.user = user;

        // Configuration de la fenêtre
        setTitle("Menu d'accueil");
        setSize(400, 200); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

        // Création des composants
        JPanel panel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // Contraintes de la grille

        JLabel welcomeLabel = new JLabel("Bienvenue, " + this.user.getPrenom()  + "!"); // Affiche le nom de l'utilisateur
        JButton empruntsButton = new JButton("Mes Emprunts");
        JButton livresButton = new JButton("Catalogue");
        JButton retardsButton = new JButton("Mes Retards");
        JButton gestionButton = new JButton("Gestion Membre");
        // Ajoutez ici d'autres fonctionnalités disponibles

        // Ajout des composants avec des contraintes
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants
        gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche
        panel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        panel.add(empruntsButton, gbc);

        gbc.gridy = 2;
        panel.add(livresButton, gbc);

        gbc.gridy = 3;
        panel.add(retardsButton, gbc);
        
        gbc.gridy = 4;
        panel.add(gestionButton, gbc);

        // Ajout du panneau à la fenêtre
        add(panel);

        // Ajout d'un écouteur d'événements au bouton "Mes Emprunts"
        empruntsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajoutez ici le code pour la fonctionnalité "Mes Emprunts"
            }
        });

        // Ajout d'un écouteur d'événements au bouton "Liste des Livres"
        livresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LibraryManagementApp menu = new LibraryManagementApp(user);
                menu.setVisible(true);
                // Fermer la fenêtre de connexion
                dispose();
            }
        });

        // Ajout d'un écouteur d'événements au bouton "Mes Retards"
        retardsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajoutez ici le code pour la fonctionnalité "Mes Retards"
            }
        });
        
        // Ajout d'un écouteur d'événements au bouton "Mes Retards"
        gestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajoutez ici le code pour la fonctionnalité "Mes Retards"
            }
        });
    }
}
