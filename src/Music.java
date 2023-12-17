/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 
 */
public class Music {
    private int id;
    private String title;
    private String runtime;
    private String category;
    private String venue;
    private int age;

    // Constructors
    public Music() {
        // Default constructor
    }

    public Music(int id, String title, String runtime, String category, String venue, int age) {
        this.id = id;
        this.title = title;
        this.runtime = runtime;
        this.category = category;
        this.venue = venue;
        this.age = age;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // toString method for better representation
    @Override
    public String toString() {
        return 
                " Title ='" + title + '\'' +
                ", runtime='" + runtime + '\'' +
                ", category='" + category + '\'' +
                ", venue='" + venue + '\'' +
                ", age=" + age;
    }
}

