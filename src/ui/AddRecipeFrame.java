package ui;

import dao.RecipeDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class AddRecipeFrame extends JFrame {
    private JTextField nameField, calField, proteinField, carbField, fatField, ingredientsField;
    private JTextArea descArea, instrArea;
    private JComboBox<String> dietCombo;
    private JLabel imgPreview;
    private File selectedImage;

    public AddRecipeFrame() {

        setTitle("Add New Recipe");
        setSize(750, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        UIManager.put("Label.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 250, 253));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;

        gbc.gridy = row++;
        formPanel.add(makeLabel("📝 Recipe Name:"), gbc);
        nameField = makeTextField();
        gbc.gridy = row++;
        formPanel.add(nameField, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("🥗 Diet Type:"), gbc);
        dietCombo = new JComboBox<>(new String[]{"Vegan", "Vegetarian", "Non-Veg"});
        dietCombo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        gbc.gridy = row++;
        formPanel.add(dietCombo, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("📖 Description:"), gbc);
        descArea = makeTextArea();
        gbc.gridy = row++;
        formPanel.add(new JScrollPane(descArea), gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("🧑‍🍳 Instructions:"), gbc);
        instrArea = makeTextArea();
        gbc.gridy = row++;
        formPanel.add(new JScrollPane(instrArea), gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("🔥 Calories (kcal):"), gbc);
        calField = makeTextField();
        gbc.gridy = row++;
        formPanel.add(calField, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("💪 Protein (g):"), gbc);
        proteinField = makeTextField();
        gbc.gridy = row++;
        formPanel.add(proteinField, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("🍚 Carbs (g):"), gbc);
        carbField = makeTextField();
        gbc.gridy = row++;
        formPanel.add(carbField, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("🧈 Fat (g):"), gbc);
        fatField = makeTextField();
        gbc.gridy = row++;
        formPanel.add(fatField, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("🥕 Ingredients (comma-separated):"), gbc);
        ingredientsField = makeTextField();
        gbc.gridy = row++;
        formPanel.add(ingredientsField, gbc);

        gbc.gridy = row++;
        formPanel.add(makeLabel("📸 Recipe Image:"), gbc);

        imgPreview = new JLabel("No image selected", SwingConstants.CENTER);
        imgPreview.setPreferredSize(new Dimension(150, 150));
        imgPreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridy = row++;
        formPanel.add(imgPreview, gbc);

        JButton chooseImgBtn = new JButton("🖼 Choose Image");
        chooseImgBtn.addActionListener(e -> chooseImage());
        gbc.gridy = row++;
        formPanel.add(chooseImgBtn, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton saveBtn = new JButton("💾 Save Recipe");
        JButton cancelBtn = new JButton("❌ Cancel");

        saveBtn.setBackground(new Color(198, 255, 198));
        cancelBtn.setBackground(new Color(255, 210, 210));

        saveBtn.addActionListener(this::saveRecipe);
        cancelBtn.addActionListener(e -> dispose());

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    private JTextArea makeTextArea() {
        JTextArea area = new JTextArea(4, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return area;
    }

    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        int option = fc.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedImage = fc.getSelectedFile();
            imgPreview.setText("");
            imgPreview.setIcon(new ImageIcon(new ImageIcon(selectedImage.getAbsolutePath())
                    .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }
    }

    private void saveRecipe(ActionEvent e) {
        try {
            RecipeDAO.addRecipe(
                    nameField.getText(),
                    (String) dietCombo.getSelectedItem(),
                    descArea.getText(),
                    instrArea.getText(),
                    Integer.parseInt(calField.getText()),
                    Integer.parseInt(proteinField.getText()),
                    Integer.parseInt(carbField.getText()),
                    Integer.parseInt(fatField.getText()),
                    selectedImage,
                    ingredientsField.getText()
            );
            JOptionPane.showMessageDialog(this, "✅ Recipe added successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
