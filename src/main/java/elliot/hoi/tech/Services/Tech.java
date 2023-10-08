package elliot.hoi.tech.Services;

import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
public class Tech {
    public enum Kind {
        Theory,
        Application
    }

    public enum Area {
        AirDoctrine,
        LandDoctrine,
        NavalDoctrine,
        Armor,
        Artillery,
        Infantry,
        Electronics,
        Industry,
        HeavyAircraft,
        LightAircraft,
        Rocketry,
        Nuclear,
        Naval,
        Submarine
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public Integer getCost() {
        return cost;
    }

    public Kind getKind() {
        return kind;
    }

    public Area getArea() {
        return area;
    }

    public Integer getTime() {
        return time;
    }

    public String getEffects() {
        return effects;
    }

    public ArrayList<Tech> getPredecessors() {
        return predecessors;
    }

    private Area area;
    private Kind kind;
    @Id
    private Long id;
    private String name;
    @Column(length = 2048)
    private String details; // desc
    @Column(length = 2048)
    private String effects;
    private Integer cost;
    private Integer time;
    private Integer chance;
    private Integer neg_offset;
    private Integer pos_offset;
    @ManyToMany
    private ArrayList<Tech> predecessors;
    @ManyToOne
    private Tech theory;
}
