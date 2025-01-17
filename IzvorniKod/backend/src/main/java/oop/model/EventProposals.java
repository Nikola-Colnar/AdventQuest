package oop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "EventProposals")
public class EventProposals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    public EventProposals(String title) {
        this.title = title;
    }

    public EventProposals() {

    }

    public Long getId() {return id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    @Override
    public String toString() {
        return "EventProposals{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
