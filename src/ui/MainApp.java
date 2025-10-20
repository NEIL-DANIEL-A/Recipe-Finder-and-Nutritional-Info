package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainApp extends JFrame {
    private ResultsPanel resultsPanel;
    private SearchPanel searchPanel;
    public MainApp() {
        setTitle("Recipe Finder & Nutrition Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        UIManager.put("Button.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        resultsPanel = new ResultsPanel();
        searchPanel = new SearchPanel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                switch (cmd) {
                    case "search" -> resultsPanel.performSearch(
                            searchPanel.getSearchText(),
                            searchPanel.getSelectedDiet()
                    );
                    case "favorites" -> new FavoritesFrame().setVisible(true);
                    case "add" -> new AddRecipeFrame().setVisible(true);
                }
            }
        });
        add(searchPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
            setVisible(true);
        });
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            try {
                new MainApp().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Startup Error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}