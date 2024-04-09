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
import javax.swing.SwingUtilities;

/**
 * Fenêtre d'inscription permettant à un utilisateur de s'enregistrer avec un nouveau compte.
 */
public class RegisterPage extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    /**
     * Constructeur pour créer la fenêtre d'inscription.
     */
    public RegisterPage() {
        // Configuration de la fenêtre
        setTitle("Inscription");
        setSize(400, 250); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

        // Création du panneau principal avec GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants

        // Création des composants
        JLabel firstNameLabel = new JLabel("Prénom:");
        firstNameField = new JTextField(20); // Taille du champ prénom
        JLabel lastNameLabel = new JLabel("Nom:");
        lastNameField = new JTextField(20); // Taille du champ nom
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20); // Taille du champ email
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20); // Taille du champ mot de passe
        JButton registerButton = new JButton("Inscription");
        JButton cancelButton = new JButton("Annuler");

        // Ajout des composants au panneau avec les contraintes
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(firstNameLabel, gbc);

        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(cancelButton, gbc);

        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        // Ajout du panneau à la fenêtre
        add(panel);

        // Ajout d'un écouteur d'événements au bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Ajout d'un écouteur d'événements au bouton d'annulation
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginPage();
            }
        });
    }

    /**
     * Méthode pour enregistrer un nouvel utilisateur avec les informations saisies.
     */
    private void registerUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (User.createUser(firstName, lastName, email, password)) {
            JOptionPane.showMessageDialog(this, "Utilisateur inscrit avec succès.");
            openLoginPage(); // Ouvrir la page de connexion après l'inscription réussie
        } else {
            JOptionPane.showMessageDialog(this, "Échec de l'inscription de l'utilisateur.");
        }
    }

    /**
     * Méthode pour ouvrir la page de connexion après l'inscription ou l'annulation.
     */
    private void openLoginPage() {
        LoginFrame loginPage = new LoginFrame();
        loginPage.setVisible(true);
        dispose(); // Fermer la fenêtre d'inscription
    }

    /**
     * Méthode principale pour exécuter la fenêtre d'inscription.
     * @param args Arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterPage().setVisible(true);
            }
        });
    }
}
