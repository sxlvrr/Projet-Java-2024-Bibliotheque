import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuAccueil extends JFrame {
    private User user;

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

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Bienvenue, " + this.user.getPrenom() + "!");
        headerPanel.add(welcomeLabel);
        return headerPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 lignes, 1 colonne
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Ajoute des marges

        JButton empruntsButton = createButton("Mes Emprunts");
        JButton livresButton = createButton("Catalogue");
        JButton retardsButton = createButton("Mes Retards");
        JButton gestionButton = createButton("Gestion Membre");

        buttonPanel.add(empruntsButton);
        buttonPanel.add(livresButton);
        buttonPanel.add(retardsButton);
        buttonPanel.add(gestionButton);

        return buttonPanel;
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(label);
            }
        });
        return button;
    }

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
                // Ajoutez ici le code pour la fonctionnalité "Gestion Membre"
                break;
            default:
                break;
        }
    }
}
