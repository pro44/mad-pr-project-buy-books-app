package com.example.buybooksonline;

public class Model_Order {
    String bill_amount;
    String book_id;
    String order_id;
    String order_time;
    String quantity;

    public Model_Order(String bill_amount, String book_id, String order_id, String order_time, String quantity) {
        this.bill_amount = bill_amount;
        this.book_id = book_id;
        this.order_id = order_id;
        this.order_time = order_time;
        this.quantity = quantity;
    }

    public Model_Order() {

    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
