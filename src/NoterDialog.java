import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NoterDialog extends JDialog {
    private JSpinner noteSpinner;
    private JTextArea commentaireArea;
    private JButton validerButton;
    private JButton annulerButton;
    private Integer note;
    private String commentaire;

    public NoterDialog(JFrame parent, String title) {
        super(parent, "Noter " + title, true); // Titre de la boîte de dialogue, modalité

        // Création du spinner pour la note
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 10, 1);
        noteSpinner = new JSpinner(spinnerModel);

        // Création des composants
        commentaireArea = new JTextArea(4, 20);
        validerButton = new JButton("Valider");
        annulerButton = new JButton("Annuler");

        // Ajout des écouteurs d'événements
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                note = (Integer) noteSpinner.getValue(); // Récupération de la note sélectionnée
                commentaire = commentaireArea.getText(); // Récupération du commentaire saisi
                dispose(); // Ferme la boîte de dialogue
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                note = null; // Réinitialisation de la note
                commentaire = null; // Réinitialisation du commentaire
                dispose(); // Ferme la boîte de dialogue
            }
        });

        // Création du panneau de contenu
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajout des composants au panneau de contenu
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.add(new JLabel("Note : "));
        inputPanel.add(noteSpinner);

        JPanel commentairePanel = new JPanel(new BorderLayout());
        commentairePanel.add(new JLabel("Commentaire : "), BorderLayout.NORTH);
        commentairePanel.add(new JScrollPane(commentaireArea), BorderLayout.CENTER);

        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(commentairePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(validerButton);
        buttonPanel.add(annulerButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Définition du panneau de contenu pour la boîte de dialogue
        setContentPane(contentPane);

        // Ajustement automatique de la taille en fonction des composants
        pack();

        // Positionnement au centre de la fenêtre parent
        setLocationRelativeTo(parent);
    }

    // Méthode pour afficher la boîte de dialogue et récupérer la note et le commentaire saisis
    public Object[] showNoteDialog() {
        setVisible(true); // Affiche la boîte de dialogue
        if (note != null && commentaire != null) {
            return new Object[]{note, commentaire}; // Retourne la note et le commentaire saisis
        } else {
            return null; // Retourne null si annulé
        }
    }
}
