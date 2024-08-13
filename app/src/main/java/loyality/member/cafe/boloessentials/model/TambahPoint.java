package loyality.member.cafe.boloessentials.model;

public class TambahPoint {
    private String IDTransaksi;
    private String nomorID;
    private String nama;
    private int point;
    private Boolean status;
    private Boolean hasil;

    public TambahPoint() {
    }

    // Constructor dengan parameter
    public TambahPoint( String nama,String IDTransaksi, int point, Boolean status, String nomorID, Boolean hasil) {
        this.nama = nama;
        this.IDTransaksi = IDTransaksi;
        this.point = point;
        this.status = status;
        this.nomorID = nomorID;
        this.hasil = hasil;
    }

    // Getter dan Setter untuk setiap atribut

    public String getIDTransaksi() {
        return IDTransaksi;
    }
    public void setIDTransaksi(String ID) {
        this.IDTransaksi = IDTransaksi;
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

    public Boolean getHasil() {
        return hasil;
    }

    public void setHasil(Boolean hasil) {
        this.hasil = hasil;
    }
}