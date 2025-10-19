package ui;

import dao.RecipeDAO;
import model.Recipe;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class EditRecipeFrame extends JFrame {
    private JTextField nameField, caloriesField, proteinField, carbsField, fatField, ingredientsField;
    private JComboBox<String> dietTypeBox;
    private JTextArea descArea, instrArea;
    private JLabel imageLabel;
    private File selectedImage;
    private final Recipe recipe;

    public EditRecipeFrame(Recipe recipe) {
        this.recipe = recipe;

        setTitle("✏️ Edit Recipe - " + recipe.getName());
        setSize(600, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // --- Fields ---
        nameField = new JTextField(recipe.getName());
        descArea = new JTextArea(recipe.getDescription());
        instrArea = new JTextArea(recipe.getInstructions());
        caloriesField = new JTextField(String.valueOf(recipe.getCalories()));
        proteinField = new JTextField(String.valueOf(recipe.getProtein()));
        carbsField = new JTextField(String.valueOf(recipe.getCarbs()));
        fatField = new JTextField(String.valueOf(recipe.getFat()));

        // Load ingredients
        List<String> ingList = RecipeDAO.getIngredientsForRecipe(recipe.getId());
        ingredientsField = new JTextField(String.join(", ", ingList));

        dietTypeBox = new JComboBox<>(new String[]{"Vegan", "Vegetarian", "Non-Veg"});
        dietTypeBox.setSelectedItem(recipe.getDietType());

        // --- Image section ---
        imageLabel = new JLabel("No image selected", SwingConstants.CENTER);
        if (recipe.getImage() != null)
            imageLabel.setIcon(new ImageIcon(ImageUtil.resize(recipe.getImage(), 100, 100)));

        JButton browseBtn = new JButton("📷 Change Image");
        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedImage = fc.getSelectedFile();
                imageLabel.setText(selectedImage.getName());
                imageLabel.setIcon(new ImageIcon(new ImageIcon(selectedImage.getAbsolutePath()).getImage()
                        .getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            }
        });

        // --- Add components to grid ---
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Diet Type:")); panel.add(dietTypeBox);
        panel.add(new JLabel("Description:")); panel.add(new JScrollPane(descArea));
        panel.add(new JLabel("Ingredients (comma-separated):")); panel.add(ingredientsField);
        panel.add(new JLabel("Instructions:")); panel.add(new JScrollPane(instrArea));
        panel.add(new JLabel("Calories:")); panel.add(caloriesField);
        panel.add(new JLabel("Protein (g):")); panel.add(proteinField);
        panel.add(new JLabel("Carbs (g):")); panel.add(carbsField);
        panel.add(new JLabel("Fat (g):")); panel.add(fatField);
        panel.add(imageLabel); panel.add(browseBtn);

        JButton saveBtn = new JButton("💾 Save Changes");
        saveBtn.setBackground(new Color(144, 238, 144));
        saveBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        saveBtn.addActionListener(e -> saveChanges());

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveChanges() {
        try {
            String name = nameField.getText().trim();
            String diet = (String) dietTypeBox.getSelectedItem();
            String desc = descArea.getText().trim();
            String instr = instrArea.getText().trim();
            int cal = Integer.parseInt(caloriesField.getText().trim());
            int pro = Integer.parseInt(proteinField.getText().trim());
            int carb = Integer.parseInt(carbsField.getText().trim());
            int fat = Integer.parseInt(fatField.getText().trim());
            String ingredients = ingredientsField.getText().trim();

            RecipeDAO.updateRecipe(recipe.getId(), name, diet, desc, instr, cal, pro, carb, fat, selectedImage, ingredients);
            JOptionPane.showMessageDialog(this, "✅ Recipe updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
