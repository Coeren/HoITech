package elliot.hoi.tech.Tech;

import elliot.hoi.tech.TextMapping.TextMapping;
import elliot.hoi.tech.TextMapping.TextMappingRepository;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Entity
@Component
public class TechLevel implements Comparable<TechLevel> {
    @Override
    public int compareTo(TechLevel o) {
        return Integer.compare(id, o.id);
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        Optional<TextMapping> candidate = textMappingRepository.findById(name);
        return candidate.isEmpty() ? name : candidate.get().getValue();
    }
    public String getDetails() {
        Optional<TextMapping> candidate = textMappingRepository.findById(details);
        return candidate.isEmpty() ? details : candidate.get().getValue();
    }
    public Integer getCost() {
        return cost;
    }
    public TechArea getArea() {
        return area;
    }
    public Integer getTime() {
        return time;
    }
    public String getEffects() {
        return effects;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TechArea area;
    @Id
    private Integer id;
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

    @Transient
    @Autowired
    private TextMappingRepository textMappingRepository;
}
