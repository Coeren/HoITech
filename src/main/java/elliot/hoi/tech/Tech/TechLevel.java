package elliot.hoi.tech.Tech;

import elliot.hoi.tech.TextMapping.TextMapping;
import elliot.hoi.tech.TextMapping.TextMappingRepository;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class TechLevel extends TechBase {
    public Set<TechApplication> getApplications() {
        return apps;
    }
    void addApplication(TechApplication application) {
        apps.add(application);
    }
    public TechArea getArea() {
        return area;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TechArea area;
    @OneToMany
    private TreeSet<TechApplication> apps = new TreeSet<>();
}