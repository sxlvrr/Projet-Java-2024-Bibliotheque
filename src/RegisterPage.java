import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;

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

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(registerButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(cancelButton, gbc);

        // Ajout du panneau à la fenêtre
        add(panel);

        // Ajout d'un écouteur d'événements au bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        // Ajout d'un écouteur d'événements au bouton d'inscription
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	LoginFrame loginPage = new LoginFrame();
                loginPage.setVisible(true);
                // Fermer la fenêtre de connexion
                dispose();
            }
        });

    }

    private void registerUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (User.createUser(firstName, lastName, email, password)) {
            JOptionPane.showMessageDialog(this, "Utilisateur inscrit avec succès.");
            LoginFrame loginPage = new LoginFrame();
            loginPage.setVisible(true);
            // Fermer la fenêtre de connexion
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de l'inscription de l'utilisateur.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterPage().setVisible(true);
            }
        });
    }
}
