package elliot.hoi.tech.Tech;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TechApplication extends TechBase {
    public List<TechBase> getPredecessors() {
        return predecessors;
    }
    public TechLevel getTheory() {
        return theory;
    }
    public Integer getChance() {
        return chance;
    }
    public String getEffects() {
        return effects;
    }

    void setTheory(TechLevel theory) { this.theory = theory; }
    void setChance(Integer chance) { this.chance = chance; }
    void appendReq(Integer id) { reqs.add(id); }
    void appendEffect(String effect) {
        effects += effect;
    }
    List<Integer> getReqs() {
        if (!reqs.isEmpty())
            return reqs;

        return predecessors.stream().map(TechBase::getId).toList();
    }

    @Override
    boolean checkFields() {
        return chance > 0 && theory != null && super.checkFields();
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TechLevel theory;
    @ManyToMany
    private ArrayList<TechBase> predecessors = new ArrayList<>();
    private Integer chance;
    @Column(length = 2048)
    private String effects = "";
    @Transient
    private ArrayList<Integer> reqs = new ArrayList<>();
}
