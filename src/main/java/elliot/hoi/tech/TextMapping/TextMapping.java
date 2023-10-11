package elliot.hoi.tech.TextMapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TextMapping {
    public String getId() {
        return sid;
    }
    public void setId(String sid) {
        this.sid = sid;
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
        this.sid = id;
        this.value = value;
        this.rest = rest;
    }
    public TextMapping(String id, String value) {
        this(id, value, ";;;;;;;;;;X\r\n");
    }
    public TextMapping() {
        this.sid = "";
        this.value = "";
        this.rest = "";
    }

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String sid;
    @Column(length = 4096)
    private String value;
    private String rest;
}

