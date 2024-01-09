import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegistrationForm extends JDialog {
    private JPanel RegistrationPanel;
    private JLabel RegisterLabel;
    private JTextField tfUsername;
    private JLabel UsernameLabel;
    private JPasswordField pfPassword;
    private JLabel tfPassword;
    private JPasswordField pfConfirmPassword;
    private JLabel tfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JButton btnReturnToLogin;
    private JLabel ReturnToLoginLabel;
    private String filePath = "C:\\Users\\Tavi\\Documents\\GitHub\\Proiecte_personale_neterminate\\Cofetarie\\src\\database.txt";

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("Creeaza-ti un cont nou");
        setContentPane(RegistrationPanel);
        setMinimumSize(new Dimension(500, 540));
        setModal(true);
        setLocationRelativeTo(parent);

        // Cream ActionListeners pentru butonul de inregistrare, cel de iesire si cel ce trimite catre logare
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnReturnToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm(null);
            }
        });

        setVisible(true);
    }

    // Metoda pentru preluarea datelor introduse si gestionarea acestora
    private void registerUser() {
        String username = tfUsername.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());
        System.out.println("user: " + username);
        System.out.println("parola: " + password);
        System.out.println("parola confirmata: " + confirmPassword);
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            JOptionPane.showMessageDialog(this, "Va rog completati toate campurile!", "Incearca din nou!", JOptionPane.ERROR_MESSAGE);
            clearTextFields();
        }
        else {
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Parolele nu sunt identice!", "Incearca din nou!", JOptionPane.ERROR_MESSAGE);
                clearTextFields();
            }
            else {
                if (username.equals(password)) {
                    JOptionPane.showMessageDialog(this, "Nu poti folosi userul pe post de parola! Pune o parola mai sigura!", "Incearca din nou!", JOptionPane.ERROR_MESSAGE);
                    clearTextFields();
                }
                else {
                    if (password.length() < 8) {
                        JOptionPane.showMessageDialog(this, "Parola trebuie sa fie de minimum 8 caractere!", "Incearca din nou!", JOptionPane.ERROR_MESSAGE);
                        clearTextFields();
                    }
                    else {
                        if(!isUsernameTaken(filePath, username)) {
                            addUserToDatabase(username, password);
                            // Porneste aplicatie cofetariei
                            System.out.println("Porneste aplicatia clientului.");
                            new ClientApp();
                            dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(this,
                                    "Inregistrarea a esuat. Username-ul exista deja!",
                                    "Incearca din nou!",
                                    JOptionPane.ERROR_MESSAGE);
                            clearTextFields();
                        }
                    }
                }
            }
        }

    }

    // Metoda pentru introducerea username-ului si parolei in baza de date (database.txt)
    private void addUserToDatabase(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Scriem o linie noua cu username-ul si parola
            writer.newLine();
            writer.write(username + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Username si parola introduse in database!");
    }

    // Metoda ce verifica daca username-ul exista deja in baza de date
    private static boolean isUsernameTaken(String filePath, String username) {
        // Verificam daca usernameul exista deja
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true; // Username-ul este deja luat
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Username-ul nu este luat
    }

    // Metoda ce goleste text fieldurile atunci cand se intampina o eroare la introducerea datelor de la tastatura
    private void clearTextFields() {
        tfUsername.setText("");
        pfPassword.setText("");
        pfConfirmPassword.setText("");
    }
}
