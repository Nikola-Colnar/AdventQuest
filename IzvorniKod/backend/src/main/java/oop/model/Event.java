package oop.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvent;

    private String StartDate;
    private String EndDate;
    private String eventName;
    private String color = "green";
    private String description;

    @ManyToOne
    @JoinColumn(name = "idGrupa")  // FK ka tabeli 'SGroup'
    private Group group;

    public Event(String eventName, String startDate, String endDate, String description) {
        this(eventName, startDate, endDate, description,"green");
    }

    public Event(String eventName, String startDate, String endDate,String description, String color) {
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.eventName = eventName;
        this.description = description;
        this.color = color;

    }

    public Event() {}

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent=" + idEvent +
                ", StartDate=" + StartDate +
                ", EndDate=" + EndDate +
                ", eventName='" + eventName + '\'' +
                ", group=" + group +
                '}';
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
