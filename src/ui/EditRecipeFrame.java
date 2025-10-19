package ui;

import dao.RecipeDAO;
import model.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EditRecipeFrame extends JFrame {

    public EditRecipeFrame(Recipe recipe) {
        setTitle("✏️ Edit Recipe - " + recipe.getName());
        setSize(800, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Scrollable form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Components
        int row = 0;

        // ✅ Name field
        JTextField nameField = new JTextField(recipe.getName(), 30);
        addField(formPanel, gbc, labelFont, inputFont, "Name:", nameField, row++);

        // ✅ Diet Type dropdown
        JComboBox<String> dietBox = new JComboBox<>(new String[]{"Veg", "Non-Veg", "Vegan"});
        dietBox.setSelectedItem(recipe.getDietType());
        addField(formPanel, gbc, labelFont, inputFont, "Diet Type:", dietBox, row++);

        // ✅ Description
        JTextArea descArea = createTextArea(recipe.getDescription(), inputFont);
        addField(formPanel, gbc, labelFont, inputFont, "Description:", new JScrollPane(descArea), row++);

        // ✅ Ingredients
        List<String> ingredients = RecipeDAO.getIngredientsForRecipe(recipe.getId());
        JTextArea ingArea = createTextArea(String.join(", ", ingredients), inputFont);
        addField(formPanel, gbc, labelFont, inputFont, "Ingredients (comma-separated):", new JScrollPane(ingArea), row++);

        // ✅ Instructions
        JTextArea instrArea = createTextArea(recipe.getInstructions(), inputFont);
        addField(formPanel, gbc, labelFont, inputFont, "Instructions:", new JScrollPane(instrArea), row++);

        // ✅ Nutrition info
        JTextField calField = new JTextField(String.valueOf(recipe.getCalories()));
        addField(formPanel, gbc, labelFont, inputFont, "Calories (kcal):", calField, row++);

        JTextField protField = new JTextField(String.valueOf(recipe.getProtein()));
        addField(formPanel, gbc, labelFont, inputFont, "Protein (g):", protField, row++);

        JTextField carbField = new JTextField(String.valueOf(recipe.getCarbs()));
        addField(formPanel, gbc, labelFont, inputFont, "Carbs (g):", carbField, row++);

        JTextField fatField = new JTextField(String.valueOf(recipe.getFat()));
        addField(formPanel, gbc, labelFont, inputFont, "Fat (g):", fatField, row++);

        // ✅ Scroll pane for the entire form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ✅ Save button
        JButton saveBtn = new JButton("💾 Save Changes");
        saveBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        saveBtn.setBackground(new Color(0, 123, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.setPreferredSize(new Dimension(180, 40));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 247, 250));
        btnPanel.add(saveBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // ✅ Save logic
        saveBtn.addActionListener(e -> {
            try {
                recipe.setName(nameField.getText().trim());
                recipe.setDietType((String) dietBox.getSelectedItem());
                recipe.setDescription(descArea.getText().trim());
                recipe.setInstructions(instrArea.getText().trim());
                recipe.setCalories(Integer.parseInt(calField.getText().trim()));
                recipe.setProtein(Integer.parseInt(protField.getText().trim()));
                recipe.setCarbs(Integer.parseInt(carbField.getText().trim()));
                recipe.setFat(Integer.parseInt(fatField.getText().trim()));

                // Update ingredients list
                List<String> updatedIngredients = Arrays.asList(ingArea.getText().split("\\s*,\\s*"));

                RecipeDAO.updateRecipe(recipe, updatedIngredients);

                JOptionPane.showMessageDialog(this,
                        "✅ Recipe updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "❌ Error updating recipe: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    // 🔹 Helper to add label + field pair
    private void addField(JPanel panel, GridBagConstraints gbc, Font labelFont, Font inputFont,
                          String labelText, Component input, int row) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (input instanceof JTextField)
            ((JTextField) input).setFont(inputFont);
        else if (input instanceof JScrollPane)
            ((JTextArea) ((JScrollPane) input).getViewport().getView()).setFont(inputFont);
        else if (input instanceof JComboBox)
            ((JComboBox<?>) input).setFont(inputFont);

        panel.add(input, gbc);
    }

    // 🔹 Helper for text areas
    private JTextArea createTextArea(String text, Font font) {
        JTextArea area = new JTextArea(text, 4, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(font);
        return area;
    }
}
