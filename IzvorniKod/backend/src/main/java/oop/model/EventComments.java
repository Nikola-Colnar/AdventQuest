package oop.model;

import jakarta.persistence.*;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

@Entity
@Table(name = "Event_Comments")
public class EventComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "comment", nullable = false, length = 500)
    private String comment;

    @Column(name = "Date")
    private Date date;

    public EventComments(String comment) {
        this.comment = comment;
        this.date = new Date();
    }

    public EventComments(User user, Event event, String comment) {
        this.user = user;
        this.event = event;
        this.comment = comment;
        this.date = new Date();
    }

    public EventComments() {}

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentId() {
        return commentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
