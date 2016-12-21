package database;

public class PollutionDbItem  {
    private int id;

    private double latitude;
    private double longitude;
    private String title;
    private String type;
    private String airPollutant;
    private double airPollutionLevel;
    private double exceedanceThreashold;


    public PollutionDbItem(){}


    public PollutionDbItem(double latitude, double longitude, String title, String type,
                           String airPollutant, double airPollutionLevel, double exceedanceThreashold) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.type = type;
        this.airPollutant = airPollutant;
        this.airPollutionLevel = airPollutionLevel;
        this.exceedanceThreashold = exceedanceThreashold;
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

    public String getAirPollutant() {
        return airPollutant;
    }

    public void setAirPollutant(String airPollutant) {
        this.airPollutant = airPollutant;
    }

    public double getAirPollutionLevel() {
        return airPollutionLevel;
    }

    public void setAirPollutionLevel(double airPollutionLevel) {
        this.airPollutionLevel = airPollutionLevel;
    }

    public double getExceedanceThreashold() {
        return exceedanceThreashold;
    }

    public void setExceedanceThreashold(double exceedanceThreashold) {
        this.exceedanceThreashold = exceedanceThreashold;
    }

    @Override
    public String toString() {
        return "PollutionDbItem{" +
                "exceedanceThreashold=" + exceedanceThreashold +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", airPollutant='" + airPollutant + '\'' +
                ", airPollutionLevel=" + airPollutionLevel +
                '}';
    }
}