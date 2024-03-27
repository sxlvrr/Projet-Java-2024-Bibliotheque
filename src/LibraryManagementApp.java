import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class LibraryManagementApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldBookTitle;
    private JTextField textFieldBookAuthor;
    private JTextArea textAreaBooks;

    private void addBook(String title, String author) {
        textAreaBooks.append(title + " by " + author + "\n");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LibraryManagementApp frame = new LibraryManagementApp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public LibraryManagementApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBookTitle = new JLabel("Book Title:");
        lblBookTitle.setBounds(30, 30, 80, 20);
        contentPane.add(lblBookTitle);

        textFieldBookTitle = new JTextField();
        textFieldBookTitle.setBounds(120, 30, 200, 20);
        contentPane.add(textFieldBookTitle);
        textFieldBookTitle.setColumns(10);

        JLabel lblBookAuthor = new JLabel("Book Author:");
        lblBookAuthor.setBounds(30, 60, 80, 20);
        contentPane.add(lblBookAuthor);

        textFieldBookAuthor = new JTextField();
        textFieldBookAuthor.setBounds(120, 60, 200, 20);
        contentPane.add(textFieldBookAuthor);
        textFieldBookAuthor.setColumns(10);

        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.setBounds(350, 45, 100, 30);
        contentPane.add(btnAddBook);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 100, 500, 200);
        contentPane.add(scrollPane);

        textAreaBooks = new JTextArea();
        scrollPane.setViewportView(textAreaBooks);

        // Action lorsque le bouton "Add Book" est cliqué
        btnAddBook.addActionListener(e -> {
            String title = textFieldBookTitle.getText();
            String author = textFieldBookAuthor.getText();
            if (!title.isEmpty() && !author.isEmpty()) {
                addBook(title, author);
                // A faire : ajouter l'enregistrement dans la BDD
            }
        });
    }
}
