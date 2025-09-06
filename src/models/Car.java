package models;
public class Car {
    public String id;
    public String model;
    public int seats;
    public double pricePerKm;
    public boolean available;

    public Car(String id, String model, int seats, double pricePerKm, boolean available) {
        this.id = id;
        this.model = model;
        this.seats = seats;
        this.pricePerKm = pricePerKm;
        this.available = available;
    }
}
