import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        // Configuration de la fenêtre
        setTitle("Connexion");
        setSize(400, 200); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

        // Création des composants
        JPanel panel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // Contraintes de la grille

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20); // Taille du champ email
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20); // Taille du champ mot de passe
        loginButton = new JButton("Connexion");
        registerButton = new JButton("S'insrcire");

        // Ajout des composants avec des contraintes
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants
        gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Étendre le bouton sur deux colonnes
        panel.add(loginButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Réinitialiser la largeur de la grille
        gbc.anchor = GridBagConstraints.EAST; // Alignement à droite
        panel.add(registerButton, gbc);

        // Ajout du panneau à la fenêtre
        add(panel);

        // Ajout d'un écouteur d'événements au bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                User user = User.verifyPassword(email, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Connexion réussie !");
                    MenuAccueil menu = new MenuAccueil(user);
                    menu.setVisible(true);
                    // Fermer la fenêtre de connexion
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Email ou mot de passe incorrect !");
                }
            }
        });
        
        // Ajout d'un écouteur d'événements au bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterPage registPage = new RegisterPage();
                registPage.setVisible(true);
                // Fermer la fenêtre de connexion
                dispose();         
            }
        });
    }
}
