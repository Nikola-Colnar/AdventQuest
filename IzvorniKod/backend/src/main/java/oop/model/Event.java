package oop.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvent;

    private Date StartDate;
    private Date EndDate;
    private String eventName;
    private String color = "green";
    private String description;

    @ManyToOne
    @JoinColumn(name = "idGrupa")  // FK ka tabeli 'SGroup'
    private Group group;

    public Event(String eventName, Date startDate, Date endDate) {
        this(eventName, startDate, endDate, "green");
    }

    public Event(String eventName, Date startDate, Date endDate, String color) {
        StartDate = startDate;
        EndDate = endDate;
        this.eventName = eventName;
        this.color = color;
    }

    public Event() {}

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
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
