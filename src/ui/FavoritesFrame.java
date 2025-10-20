package ui;

import dao.FavoriteDAO;
import model.Recipe;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FavoritesFrame extends JFrame {

    public FavoritesFrame() {
        setTitle("⭐ My Favorite Recipes");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 247, 250));

        List<Recipe> favorites = FavoriteDAO.getFavorites();

        if (favorites.isEmpty()) {
            JLabel noFavs = new JLabel("You haven't added any favorites yet!", SwingConstants.CENTER);
            noFavs.setFont(new Font("Segoe UI", Font.BOLD, 18));
            noFavs.setForeground(Color.GRAY);
            add(noFavs, BorderLayout.CENTER);
        } else {
            for (Recipe r : favorites) {
                JPanel card = new JPanel(new BorderLayout(15, 10));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
                card.setMaximumSize(new Dimension(800, 180));

                // 🔹 Recipe Image
                JLabel imgLbl = new JLabel();
                Image img = ImageUtil.resize(r.getImage(), 140, 120);
                if (img != null)
                    imgLbl.setIcon(new ImageIcon(img));
                else {
                    imgLbl.setText("📷");
                    imgLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                    imgLbl.setHorizontalAlignment(SwingConstants.CENTER);
                }
                imgLbl.setPreferredSize(new Dimension(140, 120));
                card.add(imgLbl, BorderLayout.WEST);

                // 🔹 Text info panel
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);

                JLabel nameLbl = new JLabel(r.getName() + " (" + r.getDietType() + ")");
                nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

                JLabel descLbl = new JLabel("<html><b>Description:</b> " +
                        (r.getDescription() == null ? "No description." : r.getDescription()) + "</html>");
                descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                JLabel nutriLbl = new JLabel(String.format(
                        "<html><b>Nutrition:</b> Calories: %d kcal | Protein: %dg | Carbs: %dg | Fat: %dg</html>",
                        r.getCalories(), r.getProtein(), r.getCarbs(), r.getFat()));
                nutriLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                nutriLbl.setForeground(new Color(90, 90, 90));

                infoPanel.add(nameLbl);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(descLbl);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(nutriLbl);

                // 🔹 Remove button
                JButton removeBtn = new JButton("❌ Remove from Favorites");
                removeBtn.setBackground(new Color(255, 204, 204));
                removeBtn.setFocusPainted(false);
                removeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
                removeBtn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                removeBtn.addActionListener(e -> {
                    FavoriteDAO.removeFavorite(r.getId());
                    JOptionPane.showMessageDialog(this,
                            r.getName() + " removed from favorites!");
                    dispose();
                    new FavoritesFrame().setVisible(true);
                });

                infoPanel.add(Box.createVerticalStrut(10));
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
}
