package dao;
import database.DBConnection;
import model.Recipe;
import util.ImageUtil;
import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    public static void addFavorite(int recipeId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT IGNORE INTO user_favorites (user_id, recipe_id) VALUES (1, ?)")) {
            ps.setInt(1, recipeId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeFavorite(int recipeId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM user_favorites WHERE user_id=1 AND recipe_id=?")) {
            ps.setInt(1, recipeId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Recipe> getFavorites() {
        List<Recipe> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                     SELECT r.* FROM recipes r
                     JOIN user_favorites uf ON r.id = uf.recipe_id
                     WHERE uf.user_id=1
                     ORDER BY r.name
                 """)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Image img = ImageUtil.readImageFromBlob(rs.getBinaryStream("image"));
                list.add(new Recipe(
                        rs.getInt("id"), rs.getString("name"), rs.getString("diet_type"),
                        rs.getString("description"), rs.getString("instructions"),
                        rs.getInt("calories"), rs.getInt("protein"),
                        rs.getInt("carbs"), rs.getInt("fat"), img));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
