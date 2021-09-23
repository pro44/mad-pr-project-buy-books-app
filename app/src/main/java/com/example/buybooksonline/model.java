package com.example.buybooksonline;

public class model {
    private String key;
    private String seller;
    private String book;
    private String price;
    private String quantity;
    private String pages;




    public model(){

    }

    public model(String key, String seller, String book, String price, String quantity, String pages) {
        this.key = key;
        this.seller = seller;
        this.book = book;
        this.price = price;
        this.quantity = quantity;
        this.pages = pages;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }
}
