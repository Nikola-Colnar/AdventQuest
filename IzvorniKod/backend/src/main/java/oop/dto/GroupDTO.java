package oop.dto;

public class GroupDTO {
    private int idGrupa;
    private String nazivGrupa;

    // Konstruktor
    public GroupDTO(int idGrupa, String nazivGrupa) {
        this.idGrupa = idGrupa;
        this.nazivGrupa = nazivGrupa;
    }

    // Getteri i setteri
    public int getIdGrupa() {
        return idGrupa;
    }

    public void setIdGrupa(int idGrupa) {
        this.idGrupa = idGrupa;
    }

    public String getNazivGrupa() {
        return nazivGrupa;
    }

    public void setNazivGrupa(String nazivGrupa) {
        this.nazivGrupa = nazivGrupa;
    }
}