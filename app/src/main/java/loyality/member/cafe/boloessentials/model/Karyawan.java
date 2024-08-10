package loyality.member.cafe.boloessentials.model;

public class Karyawan {
    private String nomorID;
    private String nama;
    private String tanggalBergabung;
    private String email;
    private String telpon;
    private String tanggalLahir;

    // Constructor tanpa parameter dibutuhkan untuk Firebase
    public Karyawan() {
    }

    // Constructor dengan parameter
    public Karyawan(String nomorID, String nama, String tanggalBergabung, String email, String telpon, String tanggalLahir) {
        this.nomorID = nomorID;
        this.nama = nama;
        this.tanggalBergabung = tanggalBergabung;
        this.email = email;
        this.telpon = telpon;
        this.tanggalLahir = tanggalLahir;
    }

    // Getter dan Setter untuk setiap atribut

    public String getNomorID() {
        return nomorID;
    }
    public void setNomorID(String nomorID) {
        this.nomorID = nomorID;
    }
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggalBergabung() {
        return tanggalBergabung;
    }

    public void setTanggalBergabung(String tanggalBergabung) {
        this.tanggalBergabung = tanggalBergabung;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}
