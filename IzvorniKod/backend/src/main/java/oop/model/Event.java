package oop.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvent;

    private Date date;
    private String eventName;

    public Event() {}

    public Event(String eventName, Date date) {
        this.eventName = eventName;
        this.date = date;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent=" + idEvent +
                ", date=" + date +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
