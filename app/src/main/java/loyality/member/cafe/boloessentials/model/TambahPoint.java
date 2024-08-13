package loyality.member.cafe.boloessentials.model;

public class TambahPoint {
    private String ID;
    private String nama;
    private int point;

    public TambahPoint() {
    }

    // Constructor dengan parameter
    public TambahPoint( String nama,String ID, int Point) {
        this.nama = nama;
        this.ID = ID;
        this.point = point;
    }

    // Getter dan Setter untuk setiap atribut

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}