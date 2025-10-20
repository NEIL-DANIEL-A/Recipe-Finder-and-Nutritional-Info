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
        setTitle("➕ Add New Recipe");
        setSize(800, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(44, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JLabel header = new JLabel("🍽 Add a New Recipe", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        header.setForeground(new Color(255, 255, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(44, 62, 80), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.gridx = 0;
        int row = 0;
        gbc.gridy = row++;
        formPanel.add(makeLabel("📝 Recipe Name"), gbc);
        gbc.gridy = row++;
        nameField = makeTextField();
        formPanel.add(nameField, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("🥗 Diet Type"), gbc);
        gbc.gridy = row++;
        dietCombo = new JComboBox<>(new String[]{"Vegetarian", "Non-Vegetarian", "Vegan"});
        dietCombo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        dietCombo.setBackground(Color.WHITE);
        formPanel.add(dietCombo, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("📖 Description"), gbc);
        gbc.gridy = row++;
        descArea = makeTextArea(3);
        formPanel.add(new JScrollPane(descArea), gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("🧑‍🍳 Instructions"), gbc);
        gbc.gridy = row++;
        instrArea = makeTextArea(4);
        formPanel.add(new JScrollPane(instrArea), gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("🔥 Calories (kcal)"), gbc);
        gbc.gridy = row++;
        calField = makeTextField();
        formPanel.add(calField, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("💪 Protein (g)"), gbc);
        gbc.gridy = row++;
        proteinField = makeTextField();
        formPanel.add(proteinField, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("🍚 Carbs (g)"), gbc);
        gbc.gridy = row++;
        carbField = makeTextField();
        formPanel.add(carbField, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("🧈 Fat (g)"), gbc);
        gbc.gridy = row++;
        fatField = makeTextField();
        formPanel.add(fatField, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("🥕 Ingredients (comma-separated)"), gbc);
        gbc.gridy = row++;
        ingredientsField = makeTextField();
        formPanel.add(ingredientsField, gbc);
        gbc.gridy = row++;
        formPanel.add(makeLabel("📸 Recipe Image"), gbc);
        gbc.gridy = row++;
        imgPreview = new JLabel("No image selected", SwingConstants.CENTER);
        imgPreview.setPreferredSize(new Dimension(180, 180));
        imgPreview.setOpaque(true);
        imgPreview.setBackground(new Color(250, 250, 250));
        imgPreview.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        formPanel.add(imgPreview, gbc);
        JButton chooseImgBtn = createStyledButton("🖼 Choose Image", new Color(224, 240, 255), new Color(0, 102, 204));
        chooseImgBtn.addActionListener(e -> chooseImage());
        gbc.gridy = row++;
        formPanel.add(chooseImgBtn, gbc);
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottomPanel.setBackground(new Color(44, 62, 80));
        JButton saveBtn = createStyledButton("💾 Save Recipe", new Color(187, 247, 208), new Color(0, 80, 0));
        JButton cancelBtn = createStyledButton("❌ Cancel", new Color(255, 210, 210), new Color(140, 0, 0));
        saveBtn.addActionListener(this::saveRecipe);
        cancelBtn.addActionListener(e -> dispose());
        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        lbl.setForeground(new Color(0, 0, 0));
        return lbl;
    }
    private JTextField makeTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }
    private JTextArea makeTextArea(int rows) {
        JTextArea area = new JTextArea(rows, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return area;
    }
    private JButton createStyledButton(String text, Color bg, Color borderColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        button.setBackground(bg);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }
    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        int option = fc.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedImage = fc.getSelectedFile();
            imgPreview.setText("");
            imgPreview.setIcon(new ImageIcon(new ImageIcon(selectedImage.getAbsolutePath())
                    .getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
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