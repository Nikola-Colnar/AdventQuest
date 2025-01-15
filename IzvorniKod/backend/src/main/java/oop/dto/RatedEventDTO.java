package oop.dto;

public class RatedEventDTO {

    private int RatedEventId;
    private String username;
    private String eventname;
    private String review;

    public RatedEventDTO(int ratedEventId, String username, String eventname, String review) {
        RatedEventId = ratedEventId;
        this.username = username;
        this.eventname = eventname;
        this.review = review;
    }

    public int getRatedEventId() {return RatedEventId;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getEventname() {return eventname;}

    public void setEventname(String eventname) {this.eventname = eventname;}

    public String getReview() {return review;}

    public void setReview(String review) {this.review = review;}

    @Override
    public String toString() {
        return "RatedEvenDTO{" +
                "RatedEventId=" + RatedEventId +
                ", username='" + username + '\'' +
                ", eventname='" + eventname + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}
