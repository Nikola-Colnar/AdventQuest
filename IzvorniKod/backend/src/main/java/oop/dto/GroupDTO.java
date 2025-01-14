package oop.dto;

public class GroupDTO {
    private int idgroup;
    private String nazivGrupa;

    // Konstruktor
    public GroupDTO(int idgroup, String nazivGrupa) {
        this.idgroup = idgroup;
        this.nazivGrupa = nazivGrupa;
    }

    public GroupDTO() {}

    // Getteri i setteri
    public int getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(int idgroup) {
        this.idgroup = idgroup;
    }

    public String getNazivGrupa() {
        return nazivGrupa;
    }

    public void setNazivGrupa(String nazivGrupa) {
        this.nazivGrupa = nazivGrupa;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "idGrupa=" + idgroup +
                ", nazivGrupa='" + nazivGrupa + '\'' +
                '}';
    }
}
