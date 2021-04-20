package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Chat")
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Transient
    private Chat chat;

    public Chat getChat(){
        return this.chat;
    }

    public void setChat(Chat chat){
        this.chat = chat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
