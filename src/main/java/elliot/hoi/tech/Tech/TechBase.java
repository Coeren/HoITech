package elliot.hoi.tech.Tech;

import elliot.hoi.tech.TextMapping.TextMapping;
import elliot.hoi.tech.TextMapping.TextMappingRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Component
public class TechBase extends TechObjectBasic implements Comparable<TechBase> {
    @Override
    public int compareTo(TechBase o) {
        return Integer.compare(id, o.id);
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        if (textMappingRepository == null)
            return name;

        Optional<TextMapping> candidate = textMappingRepository.findById(name);
        return candidate.isEmpty() ? name : candidate.get().getValue();
    }
    public String getDetails() {
        if (textMappingRepository == null)
            return details;

        Optional<TextMapping> candidate = textMappingRepository.findById(details);
        return candidate.isEmpty() ? details : candidate.get().getValue();
    }
    public Integer getCost() {
        return cost;
    }
    public Integer getTime() {
        return time;
    }
    public String getComment() {
        return comment;
    }
    public Integer getNegOffset() {
        return negOffset;
    }
    public Integer getPosOffset() {
        return posOffset;
    }

    @Id
    private Integer id;
    private String name;
    @Column(length = 2048)
    private String details; // desc
    private Integer cost;
    private Integer time;

    private Integer negOffset;
    private Integer posOffset;
    private String comment = "";

    @Transient
    @Autowired
    private TextMappingRepository textMappingRepository;

    void setId(Integer id) {
        this.id = id;
    }
    void setName(String name) {
        this.name = name;
    }
    void setDetails(String details) {
        this.details = details;
    }
    void setCost(Integer cost) {
        this.cost = cost;
    }
    void setTime(Integer time) {
        this.time = time;
    }
    void setNegOffset(Integer negOffset) {
        this.negOffset = negOffset;
    }
    void setPosOffset(Integer posOffset) {
        this.posOffset = posOffset;
    }
    void setComment(String comment) {
        this.comment = comment;
    }

    boolean checkFields() {
        return id > 0 && StringUtils.isNoneBlank(name, details) && cost > 0 && time > 0 && negOffset > 0 && posOffset > 0;
    }
}
