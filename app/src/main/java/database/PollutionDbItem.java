package database;

public class PollutionDbItem  {
    private int id;
    private double latitude;
    private double longitude;
    private String title;
    private String type;
    public PollutionDbItem(){}

    public PollutionDbItem(double latitude,
                           double longitude, String title, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PollutionDbItem{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", title='" + title + '\'' +
                '}';
    }
}