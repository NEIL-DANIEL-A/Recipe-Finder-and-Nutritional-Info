package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> dietCombo;
    private JButton searchButton, favoritesButton, addRecipeButton;
    public SearchPanel(ActionListener listener) {
        setLayout(new BorderLayout());
        setBackground(new Color(44, 62, 80));
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(new Color(44, 62, 80));
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 73, 94));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        header.setPreferredSize(new Dimension(1000, 90));
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 18);
        Font buttonFont = new Font("Segoe UI Emoji", Font.PLAIN, 17);
        searchField = new JTextField("Enter ingredients...");
        searchField.setPreferredSize(new Dimension(420, 45));
        searchField.setFont(mainFont);
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Enter ingredients...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Enter ingredients...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        dietCombo = new JComboBox<>(new String[]{"All", "Vegetarian", "Non-Vegetarian", "Vegan"});
        dietCombo.setPreferredSize(new Dimension(130, 45));
        dietCombo.setFont(mainFont);
        dietCombo.setBackground(Color.WHITE);
        dietCombo.setFocusable(false);
        searchButton = createStyledButton("🔍 Search", buttonFont);
        favoritesButton = createStyledButton("⭐ Favorites", buttonFont);
        addRecipeButton = createStyledButton("➕ Add Recipe", buttonFont);
        searchButton.setActionCommand("search");
        favoritesButton.setActionCommand("favorites");
        addRecipeButton.setActionCommand("add");
        searchButton.addActionListener(listener);
        favoritesButton.addActionListener(listener);
        addRecipeButton.addActionListener(listener);
        header.add(searchField);
        header.add(dietCombo);
        header.add(searchButton);
        header.add(favoritesButton);
        header.add(addRecipeButton);
        centerWrapper.add(header);
        add(centerWrapper, BorderLayout.NORTH);
    }
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(236, 240, 241));
        button.setForeground(Color.BLACK);
        button.setFont(font);
        button.setPreferredSize(new Dimension(170, 45));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    public String getSearchText() {
        String text = searchField.getText().trim();
        if (text.equals("Enter ingredients...")) return "";
        return text;
    }
    public String getSelectedDiet() {
        return (String) dietCombo.getSelectedItem();
    }
}