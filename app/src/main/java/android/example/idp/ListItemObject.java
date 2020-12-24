package android.example.idp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ListItemObject {
    String name;
    String imageId="";
    Integer count;
    boolean active;
    Time time=new Time(0);
    public Time getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time.setTime(time);;
    }


    public ListItemObject() {
        Time time=new Time(0);
        this.name ="pill";
        this.imageId =" ";
        this.count = 0;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    }

