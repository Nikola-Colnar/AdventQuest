package oop.dto;

import oop.model.Message;

import java.util.Date;

public class MessageDTO {

    private int messageID;
    private String idSender;
    private String username;
    private Date date;
    private String poruka;

    public MessageDTO(int messageID, String idSender, Date date, String poruka, String username) {
        this.messageID = messageID;
        this.idSender = idSender;
        this.date = date;
        this.poruka = poruka;
        this.username = username;
    }

    public MessageDTO(Message message, String username) {
        this.messageID = message.getMessageID();
        this.idSender = message.getIdSender();
        this.date = message.getDate();
        this.poruka = message.getPoruka();
        this.username = username;
    }

    public MessageDTO() {}

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "messageID=" + messageID +
                ", idSender='" + idSender + '\'' +
                ", username='" + username + '\'' +
                ", date=" + date +
                ", poruka='" + poruka + '\'' +
                '}';
    }
}
