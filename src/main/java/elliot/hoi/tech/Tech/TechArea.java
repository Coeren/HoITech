package elliot.hoi.tech.Tech;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.TreeSet;

//public enum Area {
//    AirDoctrine,
//    LandDoctrine,
//    NavalDoctrine,
//    Armor,
//    Artillery,
//    Infantry,
//    Electronics,
//    Industry,
//    HeavyAircraft,
//    LightAircraft,
//    Rocketry,
//    Nuclear,
//    Naval,
//    Submarine
//}

@Entity
public class TechArea {
    @Id
    private Integer id;
    private String category;
    private String name;
    private String details;
    @OneToMany
    private TreeSet<TechLevel> theoryList = new TreeSet<>();

    public TechArea(Integer id, String category, String name, String details) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.details = details;
    }
    public TechArea() {
        this(0, "", "", "");
    }

    public Integer getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }
    public String getDetails() {
        return details;
    }
    public void AddLevel(TechLevel level) {
        if (!theoryList.add(level)) {
            throw new RuntimeException("Duplicate theory levels (" + level.getId() + ") in area " + name);
        }
    }
}
