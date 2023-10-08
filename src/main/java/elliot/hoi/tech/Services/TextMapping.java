package elliot.hoi.tech.Services;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Entity
public class TextMapping {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TextMapping(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public TextMapping() {
        this.id = "";
        this.value = "";
    }

    @Id
    private String id;
    @Column(length = 4096)
    private String value;
}

