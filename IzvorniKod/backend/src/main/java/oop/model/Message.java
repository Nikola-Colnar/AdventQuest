package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@Table(name = "Message")
public class Message {

    private String uid_sender;
    private Date date;
    private String poruka;

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
}
