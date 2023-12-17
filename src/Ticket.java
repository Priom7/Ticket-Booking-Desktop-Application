
import java.time.LocalDateTime;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author hin
 */
import java.util.UUID;

public class Ticket {

    private Music musical;
    private Schedule schedule;
    private int music_id;
    private int schedule_id;
    private String ticketNumber;
    private String seatNumber;
    private String ticketType;
    private LocalDateTime date;

    // Constructors
    public Ticket() {
        // Default constructor
        generateUniqueNumbers();
    }

    public Ticket(Music musical, Schedule schedule, String ticketType, LocalDateTime date, int music_id, int schedule_id) {
        this.musical = musical;
        this.schedule = schedule;
        this.ticketType = ticketType;
        this.date = date;   
        this.music_id = music_id;
        this.schedule_id = schedule_id;

        // Generate unique numbers for ticket and seat
        generateUniqueNumbers();
    }

    // Getter and Setter methods
    public Music getMusical() {
        return musical;
    }

    public void setMusical(Music musical) {
        this.musical = musical;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Generate unique numbers for ticket and seat
    private void generateUniqueNumbers() {
        this.ticketNumber = UUID.randomUUID().toString().substring(0, 8);
        this.seatNumber = UUID.randomUUID().toString().substring(0, 6);
    }

    @Override
    public String toString() {

        return "\n    Musical: " + musical.getTitle() + " Category :" + musical.getCategory()
                + "\n    Schedule: " + schedule.getDate() + " Time: " + schedule.getTimeSlot()
                + "\n    Venue: " + musical.getVenue()
                + "\n    Price : '" + schedule.getPrice() + '\''
                + "\n    Ticket Number: '" + ticketNumber + '\''
                + "\n    Seat Number: '" + seatNumber + '\''
                + "\n    Ticket Type: '" + ticketType + '\''
                + "\n    Date: " + date;
    }

}
