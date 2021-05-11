package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CHAT")
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
