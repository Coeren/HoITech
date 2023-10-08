package elliot.hoi.tech.TextMapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
    public String getRest() {
        return rest;
    }
    public void setRest(String rest) {
        this.rest = rest;
    }

    public TextMapping(String id, String value, String rest) {
        this.id = id;
        this.value = value;
        this.rest = rest;
    }
    public TextMapping(String id, String value) {
        this(id, value, ";;;;;;;;;;X\r\n");
    }
    public TextMapping() {
        this.id = "";
        this.value = "";
        this.rest = "";
    }

    @Id
    @GeneratedValue
    private Long lid;
    @Column(unique = true)
    private String id;
    @Column(length = 4096)
    private String value;
    private String rest;
}

