import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdminApp extends JFrame {
    private DefaultListModel<String> cakeListModel;
    private JList<String> cakeList;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel quantityLabel;
    private JTextField quantityTextField;
    private JLabel priceLabel;
    private JTextField priceTextField;
    private JButton editButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton closeButton;

    public AdminApp() {
        setTitle("Administrarea cofetariei");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 540);
        setLocationRelativeTo(null);

        // Initializam datele
        cakeListModel = new DefaultListModel<>();
        cakeList = new JList<>(cakeListModel);
        nameLabel = new JLabel("Nume");
        nameTextField = new JTextField(20);
        quantityLabel = new JLabel("Cantitate(g)");
        quantityTextField = new JTextField(4);
        priceLabel = new JLabel("Pret(RON/kg)");
        priceTextField = new JTextField(5);
        editButton = new JButton("Editeaza");
        addButton = new JButton("Adauga");
        removeButton = new JButton("Sterge");
        closeButton = new JButton("Iesire");

        // Adaugam produsele din fisierul cakes.txt
        addCakesFromFile();

        // Aranjarea datelor in JFrame
        setLayout(new BorderLayout());

        JPanel cakePanel = new JPanel();
        cakePanel.setLayout( new BorderLayout());
        cakePanel.add(new JScrollPane(cakeList), BorderLayout.CENTER);

        // Create a new JPanel for the editPanel content
        JPanel editPanelContent = new JPanel();
        GridLayout editPanelLayout = new GridLayout(6, 6);
        editPanelContent.setLayout(editPanelLayout);

        // Add components to the editPanelContent
        editPanelContent.add(nameLabel);
        editPanelContent.add(nameTextField);
        editPanelContent.add(quantityLabel);
        editPanelContent.add(quantityTextField);
        editPanelContent.add(priceLabel);
        editPanelContent.add(priceTextField);
        editPanelContent.add(editButton);
        editPanelContent.add(addButton);
        editPanelContent.add(removeButton);
        editPanelContent.add(closeButton);


        add(cakePanel, BorderLayout.CENTER);
        add(editPanelContent, BorderLayout.SOUTH);

        // Cream ActionListeners pentru cele 3 butoane
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedCake();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedCake();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedCake();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = "C:\\Users\\Tavi\\Documents\\GitHub\\Proiecte_personale_neterminate\\Cofetarie\\src\\cakes.txt";
                updateCakeFile(cakeListModel, filePath);
                dispose();
            }
        });
        
        setVisible(true);
    }

    // Metoda pentru editarea datelor unui produs
    private void editSelectedCake() {
        int selectedIndex = cakeList.getSelectedIndex();
        if(selectedIndex != -1) {
            String newCakeName = nameTextField.getText();

            // Extragerea numelui vechi al produsului
            String selectedCakeInfo = cakeListModel.getElementAt(selectedIndex);
            String[] parts = selectedCakeInfo.split(" - ");
            String cakeName = parts[0];
            try {
                String newCakeInfo;
                int newCakeQuantity = Integer.parseInt(quantityTextField.getText());
                double newCakePrice = Double.parseDouble(priceTextField.getText());

                // Daca nu se modifica numele produsului
                if(newCakeName.isBlank()){
                    newCakeInfo = cakeName + " - " + newCakeQuantity + " grame disponibile - " + newCakePrice + " RON/kg";
                    JOptionPane.showMessageDialog(this, cakeName + " editat cu succes");
                    clearTextFields();
                }

                // Daca se modifica numele produsului
                else {
                    newCakeInfo = newCakeName + " - " + newCakeQuantity + " grame disponibile - " + newCakePrice + " RON/kg";
                    JOptionPane.showMessageDialog(this, newCakeName + " editat cu succes");
                    clearTextFields();
                }
                cakeListModel.setElementAt(newCakeInfo, selectedIndex);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Te rog introdu cantitatea sau pretul intr-un format corect", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Te rog selecteaza un produs!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metoda pentru adaugare unui nou produs introdus de cofetar
    private void addSelectedCake() {
        String newCakeName = nameTextField.getText();
        try {
            int newCakeQuantity = Integer.parseInt(quantityTextField.getText());
            double newCakePrice = Double.parseDouble(priceTextField.getText());
            String cakeInfo = newCakeName + " - " + newCakeQuantity + " grame disponibile - " + newCakePrice + " RON/kg";
            cakeListModel.addElement(cakeInfo);
            JOptionPane.showMessageDialog(this, newCakeName + " introdus cu succes");
            clearTextFields();
        }
        catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Te rog introdu cantitatea sau pretul intr-un format corect", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metoda pentru eliminarea unui produs selectat de cofetar
    private void removeSelectedCake() {
        int selectedIndex = cakeList.getSelectedIndex();
        if(selectedIndex != -1) {
            String selectedCakeInfo = cakeListModel.getElementAt(selectedIndex);
            String[] parts = selectedCakeInfo.split(" - ");
            String cakeName = parts[0];
            JOptionPane.showMessageDialog(this,  cakeName+ " sters cu succes");
            clearTextFields();
            cakeListModel.removeElementAt(selectedIndex);
        }
        else {
            JOptionPane.showMessageDialog(this, "Te rog selecteaza un produs!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metoda pentru introducerea unui produs in lista locala de produse
    private void addCake(String name, int quantity, double pricePerKg) {
        String cakeInfo = name + " - " + quantity + " grame disponibile - " + pricePerKg + " RON/kg";
        cakeListModel.addElement(cakeInfo);
    }

    // Metoda pentru adaugarea produselor din baza de date in lista locala
    private void addCakesFromFile() {
        String filePath = "C:\\Users\\Tavi\\Documents\\GitHub\\Proiecte_personale_neterminate\\Cofetarie\\src\\cakes.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String name = parts[0];
                int grams = Integer.parseInt(parts[1]);
                double price = Double.parseDouble(parts[2]);
                addCake(name, grams, price);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda pentru actualizarea datelor produselor din baza de date
    private void updateCakeFile(DefaultListModel<String> cakeListModel, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < cakeListModel.size(); i++) {
                String cakeInfo = cakeListModel.getElementAt(i);
                String[] parts = cakeInfo.split(" - ");
                String cakeName = parts[0];
                int availableQuantity = Integer.parseInt(parts[1].split(" ")[0]);
                double pricePerKg = Double.parseDouble(parts[2].substring(0, parts[2].indexOf("RON")));

                if (availableQuantity > 0){
                    writer.write(cakeName + ", " + availableQuantity + ", " + pricePerKg + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda ce goleste text fieldurile atunci cand se intampina o eroare la introducerea datelor de la tastatura
    private void clearTextFields() {
        nameTextField.setText("");
        quantityTextField.setText("");
        priceTextField.setText("");
    }
}
