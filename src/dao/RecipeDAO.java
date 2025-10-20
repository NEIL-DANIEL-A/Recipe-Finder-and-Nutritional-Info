package dao;
import database.DBConnection;
import model.Recipe;
import util.ImageUtil;
import javax.swing.*;
import java.awt.Image;
import java.io.*;
import java.sql.*;
import java.util.*;
public class RecipeDAO {
    public static List<Recipe> searchRecipesByIngredientOrName(String query, String dietType) {
        List<Recipe> list = new ArrayList<>();
        query = query == null ? "" : query.trim().toLowerCase();
        dietType = dietType == null ? "All" : dietType.trim();
        try (Connection conn = DBConnection.getConnection()) {
            String[] terms = query.split(",");
            List<String> keywords = new ArrayList<>();
            for (String t : terms) {
                t = t.trim().toLowerCase();
                if (!t.isEmpty()) keywords.add(t);
            }
            StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT r.id, r.name, r.diet_type, r.description,
                            r.instructions, r.calories, r.protein,
                            r.carbs, r.fat, r.image
            FROM recipes r
            LEFT JOIN recipe_ingredients ri ON r.id = ri.recipe_id
            LEFT JOIN ingredients i ON ri.ingredient_id = i.id
            WHERE 1=1
        """);
            if (!keywords.isEmpty()) {
                sql.append(" AND (");
                for (int i = 0; i < keywords.size(); i++) {
                    if (i > 0) sql.append(" OR ");
                    sql.append("(LOWER(r.name) LIKE ? OR LOWER(i.name) LIKE ?)");
                }
                sql.append(")");
            }
            if (!dietType.equalsIgnoreCase("All")) {
                if (dietType.equalsIgnoreCase("Vegetarian")) {
                    sql.append(" AND (LOWER(r.diet_type) = 'vegetarian' OR LOWER(r.diet_type) = 'vegan')");
                } else if (dietType.equalsIgnoreCase("Vegan")) {
                    sql.append(" AND LOWER(r.diet_type) = 'vegan'");
                } else if (dietType.equalsIgnoreCase("Non-Veg") || dietType.equalsIgnoreCase("Non-Vegetarian")) {
                    sql.append(" AND (LOWER(r.diet_type) = 'non-veg' OR LOWER(r.diet_type) = 'non-vegetarian')");
                }
            }
            sql.append(" ORDER BY r.name ASC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int idx = 1;
            for (String kw : keywords) {
                ps.setString(idx++, "%" + kw + "%");
                ps.setString(idx++, "%" + kw + "%");
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
            String recipeSQL = """
            INSERT INTO recipes
            (name, diet_type, description, instructions, calories, protein, carbs, fat, image)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
            int recipeId = 0;
            try (PreparedStatement ps = conn.prepareStatement(recipeSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, diet);
                ps.setString(3, desc);
                ps.setString(4, instr);
                ps.setInt(5, cal);
                ps.setInt(6, pro);
                ps.setInt(7, carb);
                ps.setInt(8, fat);
                if (imageFile != null)
                    ps.setBinaryStream(9, new FileInputStream(imageFile));
                else
                    ps.setNull(9, Types.BLOB);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) recipeId = rs.getInt(1);
            }
            if (ingredients != null && !ingredients.trim().isEmpty()) {
                String[] ingArr = ingredients.split(",");
                for (String ing : ingArr) {
                    String ingName = ing.trim().toLowerCase();
                    if (ingName.isEmpty()) continue;
                    try (PreparedStatement pi = conn.prepareStatement(
                            "INSERT IGNORE INTO ingredients (name) VALUES (?)")) {
                        pi.setString(1, ingName);
                        pi.executeUpdate();
                    }
                    int ingId = 0;
                    try (PreparedStatement getId = conn.prepareStatement(
                            "SELECT id FROM ingredients WHERE name=?")) {
                        getId.setString(1, ingName);
                        ResultSet rs = getId.executeQuery();
                        if (rs.next()) ingId = rs.getInt(1);
                    }
                    if (ingId > 0) {
                        try (PreparedStatement link = conn.prepareStatement(
                                "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, '1 unit')")) {
                            link.setInt(1, recipeId);
                            link.setInt(2, ingId);
                            link.executeUpdate();
                        }
                    }
                }
            }
            System.out.println("✅ Recipe added successfully (ID: " + recipeId + ")");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding recipe: " + e.getMessage());
        }
    }
    public static void updateRecipe(Recipe recipe, List<String> ingredients) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
            UPDATE recipes
            SET name=?, diet_type=?, description=?, instructions=?, calories=?, protein=?, carbs=?, fat=?
            WHERE id=?
        """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, recipe.getName());
                ps.setString(2, recipe.getDietType());
                ps.setString(3, recipe.getDescription());
                ps.setString(4, recipe.getInstructions());
                ps.setInt(5, recipe.getCalories());
                ps.setInt(6, recipe.getProtein());
                ps.setInt(7, recipe.getCarbs());
                ps.setInt(8, recipe.getFat());
                ps.setInt(9, recipe.getId());
                ps.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement(
                    "DELETE FROM recipe_ingredients WHERE recipe_id=?")) {
                del.setInt(1, recipe.getId());
                del.executeUpdate();
            }
            for (String ing : ingredients) {
                String ingName = ing.trim().toLowerCase();
                if (ingName.isEmpty()) continue;
                try (PreparedStatement pi = conn.prepareStatement("INSERT IGNORE INTO ingredients (name) VALUES (?)")) {
                    pi.setString(1, ingName);
                    pi.executeUpdate();
                }
                int ingId = 0;
                try (PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM ingredients WHERE name=?")) {
                    ps2.setString(1, ingName);
                    ResultSet rs = ps2.executeQuery();
                    if (rs.next()) ingId = rs.getInt(1);
                }
                if (ingId > 0) {
                    try (PreparedStatement link = conn.prepareStatement(
                            "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, '1 unit')")) {
                        link.setInt(1, recipe.getId());
                        link.setInt(2, ingId);
                        link.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getIngredientsForRecipe(int recipeId) {
        List<String> ingredients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
            SELECT i.name FROM ingredients i
            JOIN recipe_ingredients ri ON i.id = ri.ingredient_id
            WHERE ri.recipe_id = ?
        """)) {
            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ingredients.add(rs.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}