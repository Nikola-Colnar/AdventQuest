package oop.dto;

public class IspisEventDTO {

    private String eventname;
    private int numOfLikes;
    private int userLiked;

    public IspisEventDTO(String eventname, int numOfLikes, int userLiked) {
        this.eventname = eventname;
        this.numOfLikes = numOfLikes;
        this.userLiked = userLiked;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public int getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(int userLiked) {
        this.userLiked = userLiked;
    }

    @Override
    public String toString() {
        return "IspisEventDTO{" +
                "eventname='" + eventname + '\'' +
                ", numOfLikes=" + numOfLikes +
                ", userLiked=" + userLiked +
                '}';
    }
}
