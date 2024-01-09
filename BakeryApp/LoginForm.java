import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginForm extends JDialog{
    private JTextField tfUsername;
    private JButton btnLogin;
    private JButton btnCancel;
    private JButton btnReturnToRegister;
    private JLabel tfUser;
    private JLabel LabelPassword;
    private JPasswordField pfPassword;
    private JPanel LoginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Contecteaza-te la contul tau");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(500, 540));
        setModal(true);
        setLocationRelativeTo(parent);

        // Crearea de ActionListeners pentru butoanele de logare, iesire si trimitere catre inregistrare
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });


        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btnReturnToRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegistrationForm(null);
            }
        });
        setVisible(true);
    }

    // Metoda pentru perluarea si gestionarea datelor de login
    private void loginUser() {
        String username = tfUsername.getText();
        String password = String.valueOf(pfPassword.getPassword());
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Va rog completati toate campurile!", "Incearca din nou!", JOptionPane.ERROR_MESSAGE);
            tfUsername.setText("");
            pfPassword.setText("");
        }
        else {
            String filePath = "C:\\Users\\Tavi\\Documents\\GitHub\\Proiecte_personale_neterminate\\Cofetarie\\src\\database.txt";
            if (isValidLogin(filePath, username, password)) {
                // Daca se logheaza cofetarul se deschide aplicatia administrator (AdminApp)
                if (!username.equals("cofetar")) {
                    System.out.println("Porneste aplicatia clientului");
                    new ClientApp();
                }
                // Daca se logheaza un alt user existent se deschide aplicatia client (ClientApp)
                else {
                    System.out.println("Porneste aplicatia cofetarului");
                    new AdminApp();
                }
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Conectarea a esuat.",
                        "Incearca din nou!",
                        JOptionPane.ERROR_MESSAGE);
                tfUsername.setText("");
                pfPassword.setText("");
            }
        }

    }

    // Metoda pentru verificarea daca datele introduse sunt valide si se asociaza unui user din baza de date
    private static boolean isValidLogin(String filePath, String enteredUser, String enteredPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    // Verifica daca exista un user valid in baza de date
                    if (enteredUser.equals(username) && enteredPassword.equals(password)) {
                        return true; // Login valid
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Login invalid
    }

}

