package loyality.member.cafe.boloessentials.model;

public class TukarHadiah {
    private String namaMenu;
    private String nama;
    private int point;
    private Boolean status;
    private String nomorID;

    public TukarHadiah() {
    }

    // Constructor dengan parameter
    public TukarHadiah( String nama,String namaMenu, int point, Boolean status, String nomorID) {
        this.nama = nama;
        this.namaMenu = namaMenu;
        this.point = point;
        this.status = status;
        this.nomorID = nomorID;
    }

    // Getter dan Setter untuk setiap atribut

    public String getmamaMenu() {
        return namaMenu;
    }
    public void setNamaMenu(String ID) {
        this.namaMenu = namaMenu;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNomorID() {
        return nomorID;
    }

    public void setNomorID(String nomorID) {
        this.nomorID = nomorID;
    }
}