package oop.dto;

import oop.model.EventComments;

import java.util.Date;

public class EventCommentDTO {

    private String username;
    private String eventname;
    private String comment;
    private int commentId;
    private Date date;

    public EventCommentDTO(String username, String eventname, String comment, int commentId, Date date) {
        this.username = username;
        this.eventname = eventname;
        this.comment = comment;
        this.commentId = commentId;
        this.date = date;
    }

    public EventCommentDTO(EventComments ec){
        this.username = ec.getUser().getUsername();
        this.eventname = ec.getEvent().getEventName();
        this.comment = ec.getComment();
        this.commentId = ec.getCommentId();
        this.date = ec.getDate();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommentId() {
        return commentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "EventCommentDTO{" +
                "username='" + username + '\'' +
                ", eventname='" + eventname + '\'' +
                ", comment='" + comment + '\'' +
                ", commentId=" + commentId +
                ", date=" + date +
                '}';
    }
}
