package algonquin.cst2335.team_project;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteLocation {

    @ColumnInfo(name = "timezone")
    private String timezone;
    @ColumnInfo(name = "sunrise")
    private String sunrise;
    @ColumnInfo(name = "sunset")
    private String sunset;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;
    @ColumnInfo (name = "fetchDate")
    private String fetchDate;


    public FavoriteLocation(String timezone, String sunrise, String sunset, String latitude, String longitude,
                            String fetchDate) {
        this.timezone = timezone;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fetchDate = fetchDate;
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getTimezone() {
        return timezone;
    }
    public String getSunrise() {return sunrise;}
    public String getSunset() {return sunset;}
    public String getFetchDate() {return fetchDate;}







    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
}
