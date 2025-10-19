package ui;

import dao.FavoriteDAO;
import dao.RecipeDAO;
import model.Recipe;
import util.ImageUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends JPanel {
    private JPanel resultsContainer;

    public ResultsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // Center wrapper for aligning results in middle
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 247, 250));

        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        resultsContainer.setBackground(new Color(245, 247, 250));

        JLabel welcome = new JLabel(
                "<html><center><h2>🔎 Welcome to Recipe Finder & Nutrition Tracker</h2>" +
                        "<p style='font-size:12px;'>Search recipes by name or ingredients above to get started!</p></center></html>",
                SwingConstants.CENTER
        );
        welcome.setForeground(new Color(70, 70, 70));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultsContainer.add(Box.createVerticalGlue());
        resultsContainer.add(welcome);
        resultsContainer.add(Box.createVerticalGlue());

        centerWrapper.add(resultsContainer, new GridBagConstraints());

        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void performSearch(String query, String diet) {
        RecipeDAO dao = new RecipeDAO();
        List<Recipe> recipes = dao.searchRecipesByIngredientOrName(query.trim(), diet);
        showResults(recipes);
    }

    public void showResults(List<Recipe> recipes) {
        resultsContainer.removeAll();

        if (recipes == null || recipes.isEmpty()) {
            JLabel lbl = new JLabel("No recipes found.", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setForeground(Color.GRAY);
            resultsContainer.add(Box.createVerticalStrut(20));
            resultsContainer.add(lbl);
        } else {
            for (Recipe recipe : recipes) {
                JPanel card = createRecipeCard(recipe);
                card.setAlignmentX(Component.CENTER_ALIGNMENT);
                resultsContainer.add(card);
                resultsContainer.add(Box.createVerticalStrut(10));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createRecipeCard(Recipe recipe) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setMaximumSize(new Dimension(950, 200));
        card.setPreferredSize(new Dimension(950, 200));

        JLabel imgLbl = new JLabel();
        imgLbl.setHorizontalAlignment(SwingConstants.CENTER);
        imgLbl.setVerticalAlignment(SwingConstants.CENTER);
        imgLbl.setPreferredSize(new Dimension(150, 120));

        Image img = recipe.getImage();
        if (img != null) {
            imgLbl.setIcon(new ImageIcon(ImageUtil.resize(img, 150, 120)));
        } else {
            imgLbl.setText("📷");
            imgLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        }

        card.add(imgLbl, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLbl = new JLabel(recipe.getName() + " (" + recipe.getDietType() + ")");
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // ✅ Description (wrapped)
        JLabel descLbl = new JLabel("<html><div style='width:700px;'><b>Description:</b> "
                + recipe.getDescription() + "</div></html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // ✅ Ingredients (between description and instructions)
        List<String> ingredients = RecipeDAO.getIngredientsForRecipe(recipe.getId());
        String ingredientsText = ingredients.isEmpty()
                ? "None listed"
                : String.join(", ", ingredients);

        JLabel ingLbl = new JLabel("<html><div style='width:700px;'><b>Ingredients:</b> "
                + ingredientsText + "</div></html>");
        ingLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // ✅ Instructions (wrapped)
        JLabel instrLbl = new JLabel("<html><div style='width:700px;'><b>Instructions:</b> "
                + recipe.getInstructions() + "</div></html>");
        instrLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // ✅ Nutrition (bottom line)
        JLabel nutriLbl = new JLabel("Calories: " + recipe.getCalories() + " kcal | Protein: "
                + recipe.getProtein() + "g | Carbs: " + recipe.getCarbs() + "g | Fat: " + recipe.getFat() + "g");
        nutriLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        nutriLbl.setForeground(new Color(90, 90, 90));

        // Add all to text panel
        textPanel.add(nameLbl);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLbl);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(ingLbl);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(instrLbl);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(nutriLbl);

        card.add(textPanel, BorderLayout.CENTER);

        // Add to favorites button
        JButton favBtn = new JButton("⭐ Add to Favorites");
        favBtn.setBackground(new Color(255, 229, 153));
        favBtn.setFocusPainted(false);
        favBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        favBtn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        favBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        favBtn.addActionListener(e -> {
            FavoriteDAO.addFavorite(recipe.getId());
            JOptionPane.showMessageDialog(this,
                    recipe.getName() + " added to favorites!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(favBtn);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }
}
