package loyality.member.cafe.boloessentials.model;

public class Karyawan {
    private String nomorIDKaryawan;
    private String nama;
    private String tanggalBergabung;
    private String email;
    private String telpon;
    private String tanggalLahir;

    // Constructor tanpa parameter dibutuhkan untuk Firebase
    public Karyawan() {
    }

    // Constructor dengan parameter
    public Karyawan(String nomorIDKaryawan, String nama, String tanggalBergabung, String email, String telpon, String tanggalLahir) {
        this.nomorIDKaryawan = nomorIDKaryawan;
        this.nama = nama;
        this.tanggalBergabung = tanggalBergabung;
        this.email = email;
        this.telpon = telpon;
        this.tanggalLahir = tanggalLahir;
    }

    // Getter dan Setter untuk setiap atribut

    public String getNomorIDKaryawan() {
        return nomorIDKaryawan;
    }
    public void setNomorIDKaryawan(String nomorIDKaryawan) {
        this.nomorIDKaryawan = nomorIDKaryawan;
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
