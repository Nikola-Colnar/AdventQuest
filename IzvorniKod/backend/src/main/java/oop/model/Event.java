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
    private int idPredstavnika;
    private String datumEventa;

    public Event(){

    }

    public Event(String nazivEventa, int idPredstavnika, String datumEventa) {
        this.nazivEventa = nazivEventa;
        this.idPredstavnika = idPredstavnika;
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

    public int getIdPredstavnika() {
        return idPredstavnika;
    }

    public void setIdPredstavnika(int idPredstavnika) {
        this.idPredstavnika = idPredstavnika;
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
                ", idPredstavnika='" + idPredstavnika + '\'' +
                ", datumEventa='" + datumEventa + '\'' +
                '}';
    }

}
