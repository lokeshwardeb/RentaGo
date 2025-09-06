package models;
public class Rental {
    public String user;
    public String carId;
    public String journey;
    public double totalCost;

    public Rental(String user, String carId, String journey, double totalCost) {
        this.user = user;
        this.carId = carId;
        this.journey = journey;
        this.totalCost = totalCost;
    }
}
