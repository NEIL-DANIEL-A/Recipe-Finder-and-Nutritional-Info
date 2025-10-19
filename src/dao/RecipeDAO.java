package dao;
import database.DBConnection;
import model.Recipe;
import util.ImageUtil;
import java.awt.Image;
import java.io.*;
import java.sql.*;
import java.util.*;

public class RecipeDAO {

    public static List<Recipe> searchRecipesByIngredientOrName(String query, String dietType) {
        List<Recipe> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT r.*
            FROM recipes r
            LEFT JOIN recipe_ingredients ri ON r.id = ri.recipe_id
            LEFT JOIN ingredients i ON ri.ingredient_id = i.id
            WHERE (
                LOWER(r.name) LIKE ? OR LOWER(i.name) LIKE ?
            )
        """);

            if (!dietType.equalsIgnoreCase("All")) {
                if (dietType.equalsIgnoreCase("Vegetarian")) {
                    sql.append(" AND (LOWER(r.diet_type) = 'vegetarian' OR LOWER(r.diet_type) = 'vegan')");
                } else {
                    sql.append(" AND LOWER(r.diet_type) = ?");
                }
            }

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, "%" + query.toLowerCase().trim() + "%");
            ps.setString(2, "%" + query.toLowerCase().trim() + "%");

            if (!dietType.equalsIgnoreCase("All") && !dietType.equalsIgnoreCase("Vegetarian")) {
                ps.setString(3, dietType.toLowerCase());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Image img = ImageUtil.readImageFromBlob(rs.getBinaryStream("image"));
                list.add(new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("diet_type"),
                        rs.getString("description"),
                        rs.getString("instructions"),
                        rs.getInt("calories"),
                        rs.getInt("protein"),
                        rs.getInt("carbs"),
                        rs.getInt("fat"),
                        img
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }




    public static void addRecipe(String name, String diet, String desc, String instr,
                                 int cal, int pro, int carb, int fat, File imageFile,
                                 String ingredients) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO recipes (name, diet_type, description, instructions, calories, protein, carbs, fat, image) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            ps.setString(2, diet);
            ps.setString(3, desc);
            ps.setString(4, instr);
            ps.setInt(5, cal);
            ps.setInt(6, pro);
            ps.setInt(7, carb);
            ps.setInt(8, fat);

            if (imageFile != null)
                ps.setBinaryStream(9, new java.io.FileInputStream(imageFile));
            else
                ps.setNull(9, java.sql.Types.BLOB);

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            int recipeId = 0;
            if (keys.next()) recipeId = keys.getInt(1);

            String[] ingArr = ingredients.split(",");
            for (String ing : ingArr) {
                String ingName = ing.trim().toLowerCase();
                if (ingName.isEmpty()) continue;

                PreparedStatement pi = conn.prepareStatement(
                        "INSERT IGNORE INTO ingredients (name) VALUES (?)");
                pi.setString(1, ingName);
                pi.executeUpdate();

                PreparedStatement getId = conn.prepareStatement(
                        "SELECT id FROM ingredients WHERE name=?");
                getId.setString(1, ingName);
                ResultSet rs = getId.executeQuery();
                int ingId = rs.next() ? rs.getInt(1) : 0;

                PreparedStatement link = conn.prepareStatement(
                        "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)");
                link.setInt(1, recipeId);
                link.setInt(2, ingId);
                link.setString(3, "1 unit");
                link.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getIngredientsForRecipe(int recipeId) {
        List<String> ingredients = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT i.name FROM ingredients i " +
                             "JOIN recipe_ingredients ri ON i.id = ri.ingredient_id " +
                             "WHERE ri.recipe_id = ?")) {

            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ingredients.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ingredients;
    }


}
