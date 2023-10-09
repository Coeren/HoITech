package elliot.hoi.tech.Tech;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;
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

class TechObjectBasic {
    void setId(Integer id) {}
    void setName(String name) {}
    void setDetails(String details) {}
}

@Entity
public class TechArea extends TechObjectBasic {
    @Id
    private Integer id;
    private String category;
    private String name;
    private String details;
    @Column(length = 1024)
    private String comment;
    @OneToMany
    private TreeSet<TechLevel> theoryList = new TreeSet<>();

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
    public Set<TechLevel> getLevels() { return theoryList; }
    void addLevel(TechLevel level) {
        if (!theoryList.add(level)) {
            throw new RuntimeException("Duplicate theory levels (" + level.getId() + ") in area " + name);
        }
    }

    void appendComment(String comment) {
        this.comment += comment;
    }
    void setId(Integer id) {
        this.id = id;
    }
    void setCategory(String category) {
        this.category = category;
    }
    void setName(String name) {
        this.name = name;
    }
    void setDetails(String details) {
        this.details = details;
    }

    boolean checkFields() {
        return id > 0 && !category.isBlank() && !name.isBlank() && !details.isBlank();
    }
}
