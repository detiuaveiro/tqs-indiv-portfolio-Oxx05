package pt.ua.tqs;


public class Product {
    public Integer id;
    public String image;
    public String description;
    public Double price;
    public String title;
    public String category;

    public Product(){

    }

    public Product(String id, String image, String description, Double price, String title, String category) {
        this.id = Integer.parseInt(id);
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
    }
}