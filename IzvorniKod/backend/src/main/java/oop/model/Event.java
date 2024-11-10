package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import oop.repository.EventRepository;

@Entity //jako bitno da JPA prepozna
@Table(name = "events")
public class Event {

    @Id   //oznacava id, ova dva @ su bitna
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nazivEventa;
    private String uidPredstavnika;
    private String datumEventa;

    public Event(){

    }

    public Event(String nazivEventa, String uidPredstavnika, String datumEventa) {
        this.nazivEventa = nazivEventa;
        this.uidPredstavnika = uidPredstavnika;
        this.datumEventa = datumEventa;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivEventa() {
        return nazivEventa;
    }

    public void setNazivEventa( String nazivEventa) {
        this.nazivEventa = nazivEventa;
    }

    public String getUidPredstavnika() {
        return uidPredstavnika;
    }

    public void setIdPredstavnika(String uidPredstavnika) {
        this.uidPredstavnika = uidPredstavnika;
    }

    public String getDatumEventa() {
        return datumEventa;
    }

    public void setDatumEventa(String datumEventa) {
        this.datumEventa = datumEventa;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", nazivEventa='" + nazivEventa + '\'' +
                ", uidPredstavnika='" + uidPredstavnika + '\'' +
                ", datumEventa='" + datumEventa + '\'' +
                '}';
    }

}
