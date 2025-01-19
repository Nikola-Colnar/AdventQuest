package oop.dto;

import oop.model.RatedEvent;

public class RatedEventDTO {

    private String username;
    private int lajk;
    private String eventName;

    public RatedEventDTO(String username, String eventName, int lajk) {
        this.username = username;
        this.eventName = eventName;
        this.lajk = lajk;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public int getLajk() {return lajk;}

    public void setLajk(int lajk) {this.lajk = lajk;}

    public String getEventName() {return eventName;}

    public void setEventName(String eventName) {this.eventName = eventName;}

    @Override
    public String toString() {
        return "RatedEventDTO{" +
                "username='" + username + '\'' +
                ", lajk=" + lajk +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
