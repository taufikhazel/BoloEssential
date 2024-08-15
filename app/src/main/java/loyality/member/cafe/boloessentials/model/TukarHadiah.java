package loyality.member.cafe.boloessentials.model;

public class TukarHadiah {
    private String NamaMenu;
    private String Nama;
    private int Point;
    private Boolean Status;
    private Boolean Hasil;
    private String nomorID;
    private int IDTransaksi;

    public TukarHadiah() {
    }

    // Constructor dengan parameter
    public TukarHadiah( String Nama,String NamaMenu, int Point, Boolean Status,Boolean Hasil, String nomorID, int IDTransaksi) {
        this.Nama = Nama;
        this.NamaMenu = NamaMenu;
        this.Point = Point;
        this.Status = Status;
        this.Hasil = Hasil;
        this.nomorID = nomorID;
        this.IDTransaksi = IDTransaksi;
    }

    // Getter dan Setter untuk setiap atribut

    public String getNamaMenu() {
        return NamaMenu;
    }
    public void setNamaMenu(String NamaMenu) {
        this.NamaMenu = NamaMenu;
    }
    public String getNama() {
        return Nama;
    }

    public void setNama(String Nama) {
        this.Nama = Nama;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int Point) {
        this.Point = Point;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean Status) {
        this.Status = Status;
    }

    public Boolean getHasil() {
        return Hasil;
    }

    public void setHasil(Boolean Hasil) {
        this.Hasil = Hasil;
    }

    public String getNomorID() {
        return nomorID;
    }

    public void setNomorID(String nomorID) {
        this.nomorID = nomorID;
    }
    public int getIDTransaksi() {
        return IDTransaksi;
    }

    public void setIDTransaksi(int IDTransaksi) {
        this.IDTransaksi = IDTransaksi;
    }
}