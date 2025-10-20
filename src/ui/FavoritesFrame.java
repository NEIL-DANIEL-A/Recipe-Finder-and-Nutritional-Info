package ui;

import dao.FavoriteDAO;
import model.Recipe;
import util.ImageUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class FavoritesFrame extends JFrame {

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }


    public FavoritesFrame() {
        setTitle("⭐ My Favorite Recipes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 247, 250));

        List<Recipe> favorites = FavoriteDAO.getFavorites();

        if (favorites.isEmpty()) {
            JLabel lbl = new JLabel("No favorites yet!", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setForeground(Color.GRAY);
            add(lbl, BorderLayout.CENTER);
        } else {
            for (Recipe r : favorites) {
                JPanel card = new JPanel(new BorderLayout(15, 10));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(12, 18, 12, 18)
                ));
                card.setMaximumSize(new Dimension(1050, Integer.MAX_VALUE));

                JLabel imgLbl = new JLabel();
                Image img = ImageUtil.resize(r.getImage(), 180, 130);
                if (img != null)
                    imgLbl.setIcon(new ImageIcon(img));
                else {
                    imgLbl.setText("📷");
                    imgLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                }
                card.add(imgLbl, BorderLayout.WEST);

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                info.setBackground(Color.WHITE);

                JLabel name = new JLabel(r.getName() + " (" + r.getDietType() + ")");
                name.setFont(new Font("Segoe UI", Font.BOLD, 17));

                JLabel desc = new JLabel("<html><b>Description:</b> " + r.getDescription() + "</html>");
                desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                JLabel nutri = new JLabel("<html><b>Nutrition:</b> Calories: " + r.getCalories()
                        + " kcal | Protein: " + r.getProtein() + "g | Carbs: " + r.getCarbs()
                        + "g | Fat: " + r.getFat() + "g</html>");
                nutri.setFont(new Font("Segoe UI", Font.ITALIC, 12));

                JButton removeBtn = new JButton("❌ Remove from Favorites");
                removeBtn.setBackground(new Color(255, 182, 193));
                removeBtn.setFocusPainted(false);
                removeBtn.addActionListener(e -> {
                    FavoriteDAO.removeFavorite(r.getId());
                    JOptionPane.showMessageDialog(this, r.getName() + " removed!");
                    dispose();
                    new FavoritesFrame().setVisible(true);
                });

                info.add(name);
                info.add(Box.createVerticalStrut(6));
                info.add(desc);
                info.add(Box.createVerticalStrut(4));
                info.add(nutri);
                info.add(Box.createVerticalStrut(10));
                info.add(removeBtn);

                card.add(info, BorderLayout.CENTER);
                content.add(card);
                content.add(Box.createVerticalStrut(10));
            }

            add(new JScrollPane(content), BorderLayout.CENTER);
        }
    }
}
