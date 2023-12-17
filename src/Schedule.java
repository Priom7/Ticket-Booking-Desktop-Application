/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 
 */
import java.time.LocalDateTime;
import java.util.Date;

public class Schedule {
    private int id;
    private int musicId; // Foreign key referencing the Music table
    private LocalDateTime date;
    private String timeSlot;
    private int availableSeats;
    private double price;

    // Constructors
    public Schedule() {
        // Default constructor
    }

    public Schedule(int id, int musicId, LocalDateTime date, String timeSlot, int availableSeats, double price) {
        this.id = id;
        this.musicId = musicId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public double getPrice(){
        return price;
    }
    
    public void setPrice(double price){
        this.price = price;
    }

    // toString method for better representation
    @Override
    public String toString() {
        return 
                " Price=" + price +
                ", Date=" + date +
                ", Time Slot='" + timeSlot + '\'' +
                ", Available Seats=" + availableSeats ;
    }
}
