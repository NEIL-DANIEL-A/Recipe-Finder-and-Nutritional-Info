package model;
import java.awt.Image;

public class Recipe {
    private int id;
    private String name, dietType, description, instructions;
    private int calories, protein, carbs, fat;
    private Image image;

    public Recipe(int id, String name, String dietType, String desc, String instr,
                  int cal, int pro, int carb, int fat, Image image) {
        this.id = id;
        this.name = name;
        this.dietType = dietType;
        this.description = desc;
        this.instructions = instr;
        this.calories = cal;
        this.protein = pro;
        this.carbs = carb;
        this.fat = fat;
        this.image = image;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDietType() { return dietType; }
    public String getDescription() { return description; }
    public String getInstructions() { return instructions; }
    public int getCalories() { return calories; }
    public int getProtein() { return protein; }
    public int getCarbs() { return carbs; }
    public int getFat() { return fat; }
    public Image getImage() { return image; }
}
