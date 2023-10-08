package elliot.hoi.tech.Tech;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TechApplication extends TechLevel {
    public List<TechLevel> getPredecessors() {
        return predecessors;
    }
    public TechLevel getTheory() {
        return theory;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TechLevel theory;
    @ManyToMany
    private ArrayList<TechLevel> predecessors = new ArrayList<>();
}
