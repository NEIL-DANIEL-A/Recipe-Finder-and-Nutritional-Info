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
                JPanel card = new JPanel(new BorderLayout(10, 10));
                card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                card.setBackground(Color.WHITE);
                card.setMaximumSize(new Dimension(700, 150));

                JLabel imgLbl = new JLabel();
                Image img = ImageUtil.resize(r.getImage(), 120, 120);
                if (img != null)
                    imgLbl.setIcon(new ImageIcon(img));
                else
                    imgLbl.setText("📷");

                card.add(imgLbl, BorderLayout.WEST);

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                info.setBackground(Color.WHITE);

                JLabel name = new JLabel(r.getName() + " (" + r.getDietType() + ")");
                name.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JLabel desc = new JLabel("<html><b>Description:</b> " + r.getDescription() + "</html>");
                JLabel nutri = new JLabel("Calories: " + r.getCalories() + " kcal | Protein: "
                        + r.getProtein() + "g | Carbs: " + r.getCarbs() + "g | Fat: " + r.getFat() + "g");

                info.add(name);
                info.add(desc);
                info.add(nutri);

                JButton removeBtn = createStyledButton("❌ Remove", new Color(255, 182, 193));
                removeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

                removeBtn.addActionListener(e -> {
                    FavoriteDAO.removeFavorite(r.getId());
                    JOptionPane.showMessageDialog(this, r.getName() + " removed from favorites!");
                    dispose();
                    new FavoritesFrame().setVisible(true);
                });

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
