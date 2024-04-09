import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Fenêtre de connexion utilisateur avec des champs pour l'email, le mot de passe et des boutons pour la connexion et l'inscription.
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    /**
     * Constructeur pour initialiser la fenêtre de connexion.
     * Configurer les composants et les actions des boutons.
     */
    public LoginFrame() {
        // Configuration de la fenêtre
        setTitle("Connexion");
        setSize(400, 200); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

        // Création du panneau principal avec GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Création des composants
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20); // Champ de texte pour l'email
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20); // Champ de texte pour le mot de passe
        loginButton = new JButton("Connexion");
        registerButton = new JButton("S'inscrire");

        // Ajout des composants au panneau avec des contraintes GridBagConstraints
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
        panel.add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Réinitialiser la largeur de la grille
        gbc.anchor = GridBagConstraints.EAST; // Alignement à droite
        panel.add(loginButton, gbc);

        // Ajout du panneau à la fenêtre
        add(panel);

        // Ajout d'un écouteur d'événements au bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            /**
             * Méthode exécutée lorsque le bouton de connexion est cliqué.
             * Récupère les informations de l'utilisateur, vérifie les informations de connexion et affiche un message approprié.
             */
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Vérifier les informations de connexion
                User user = User.verifyPassword(email, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Connexion réussie !");
                    MenuAccueil menu = new MenuAccueil(user);
                    menu.setVisible(true);
                    dispose(); // Fermer la fenêtre de connexion après la connexion réussie
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Email ou mot de passe incorrect !");
                }
            }
        });

        // Ajout d'un écouteur d'événements au bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            /**
             * Méthode exécutée lorsque le bouton d'inscription est cliqué.
             * Ouvre une nouvelle fenêtre d'inscription et ferme la fenêtre de connexion.
             */
            public void actionPerformed(ActionEvent e) {
                // Ouvrir la page d'inscription
                RegisterPage registPage = new RegisterPage();
                registPage.setVisible(true);
                dispose(); // Fermer la fenêtre de connexion
            }
        });
    }
}
