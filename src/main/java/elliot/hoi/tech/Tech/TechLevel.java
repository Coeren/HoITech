package elliot.hoi.tech.Tech;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TechLevel extends TechBase {
    public TechLevel(TechArea area) {
        this.area = area;
    }
    public TechLevel() {

    }

    public List<TechApplication> getApplications() {
        return apps;
    }
    void addApplication(TechApplication application) {
        apps.add(application);
    }
    public TechArea getArea() {
        return area;
    }

    @ManyToOne
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private TechArea area;
    @OneToMany
    private ArrayList<TechApplication> apps = new ArrayList<>();
//    private TreeSet<TechApplication> apps = new TreeSet<>();
}