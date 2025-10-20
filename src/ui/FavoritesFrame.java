package ui;
import dao.FavoriteDAO;
import dao.RecipeDAO;
import model.Recipe;
import util.ImageUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;
public class FavoritesFrame extends JFrame {
    public FavoritesFrame() {
        setTitle("⭐ My Favorite Recipes");
        setSize(950, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 244, 248));
        List<Recipe> favorites = FavoriteDAO.getFavorites();
        if (favorites.isEmpty()) {
            JLabel noFavs = new JLabel("You haven't added any favorites yet!", SwingConstants.CENTER);
            noFavs.setFont(new Font("Segoe UI", Font.BOLD, 18));
            noFavs.setForeground(Color.GRAY);
            add(noFavs, BorderLayout.CENTER);
        } else {
            for (Recipe r : favorites) {
                JPanel card = new RoundedPanel(20, new Color(255, 255, 255));
                card.setLayout(new BorderLayout(20, 10));
                card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
                card.setMaximumSize(new Dimension(850, 250));
                JLabel imgLbl = new JLabel();
                Image img = ImageUtil.resize(r.getImage(), 180, 140);
                if (img != null)
                    imgLbl.setIcon(new ImageIcon(img));
                else {
                    imgLbl.setText("📷");
                    imgLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
                    imgLbl.setHorizontalAlignment(SwingConstants.CENTER);
                }
                imgLbl.setPreferredSize(new Dimension(180, 140));
                card.add(imgLbl, BorderLayout.WEST);
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(new Color(255, 255, 255));
                JLabel nameLbl = new JLabel("<html><b>" + r.getName() + " (" + r.getDietType() + ")</b></html>");
                nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
                nameLbl.setForeground(new Color(50, 50, 50));
                JLabel descLbl = new JLabel("<html><b>Description:</b> " +
                        (r.getDescription() == null || r.getDescription().isBlank()
                                ? "No description provided." : r.getDescription()) + "</html>");
                descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                List<String> ingredients = RecipeDAO.getIngredientsForRecipe(r.getId());
                String ingText = ingredients.isEmpty() ? "None listed" : String.join(", ", ingredients);
                JLabel ingLbl = new JLabel("<html><b>Ingredients:</b> " + ingText + "</html>");
                ingLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                JLabel instrLbl = new JLabel("<html><b>Instructions:</b> " +
                        (r.getInstructions() == null || r.getInstructions().isBlank()
                                ? "No instructions provided." : r.getInstructions()) + "</html>");
                instrLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                JLabel nutriLbl = new JLabel(String.format(
                        "<html><b>Nutrition:</b> Calories: %d kcal | Protein: %dg | Carbs: %dg | Fat: %dg</html>",
                        r.getCalories(), r.getProtein(), r.getCarbs(), r.getFat()));
                nutriLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                nutriLbl.setForeground(new Color(80, 80, 80));
                JButton removeBtn = new JButton("❌ Remove from Favorites");
                removeBtn.setBackground(new Color(255, 240, 240));
                removeBtn.setForeground(new Color(120, 40, 40));
                removeBtn.setFocusPainted(false);
                removeBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
                removeBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 160, 160), 1, true));
                removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                removeBtn.addActionListener(e -> {
                    FavoriteDAO.removeFavorite(r.getId());
                    JOptionPane.showMessageDialog(this,
                            r.getName() + " removed from favorites!");
                    dispose();
                    new FavoritesFrame().setVisible(true);
                });
                infoPanel.add(nameLbl);
                infoPanel.add(Box.createVerticalStrut(6));
                infoPanel.add(descLbl);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(ingLbl);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(instrLbl);
                infoPanel.add(Box.createVerticalStrut(8));
                infoPanel.add(nutriLbl);
                infoPanel.add(Box.createVerticalStrut(12));
                infoPanel.add(removeBtn);
                card.add(infoPanel, BorderLayout.CENTER);
                contentPanel.add(card);
                contentPanel.add(Box.createVerticalStrut(15));
            }
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            add(scrollPane, BorderLayout.CENTER);
        }
    }
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, radius, radius);
            g2.setColor(new Color(200, 200, 200, 100));
            g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, radius, radius);
            super.paintComponent(g);
        }
    }
}