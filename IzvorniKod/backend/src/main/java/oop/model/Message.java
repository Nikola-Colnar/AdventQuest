package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@Table(name = "Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;

    private String uid_sender;
    private Date date;
    private String poruka;

    @ManyToOne
    @JoinColumn(name = "idGrupa")
    private Group group;

    public Message() {}

    public Message(String uid_sender, Date date, String poruka) {
        this.uid_sender = uid_sender;
        this.date = date;
        this.poruka = poruka;
    }

    public int getMessageID() {
        return messageID;
    }

    public String getUid_sender() {
        return uid_sender;
    }

    public Date getDate() {
        return date;
    }

    public String getPoruka() {
        return poruka;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uid_sender='" + uid_sender + '\'' +
                ", date=" + date +
                ", poruka='" + poruka + '\'' +
                '}';
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
