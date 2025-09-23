package pt.ua.tqs;

public class Stock{
    private String label;
    private Integer quantity;

    public Stock(String s, Integer i){
        label = s;
        quantity = i;
    }

    public String getLabel(){
        return label;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public void setQuantity(Integer i){
        quantity = i;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}