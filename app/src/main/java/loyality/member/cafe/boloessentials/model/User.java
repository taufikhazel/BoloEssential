package loyality.member.cafe.boloessentials.model;

public class User {
    private String nama;
    private String tanggalBergabung;
    private String email;
    private String telpon;
    private String tanggalLahir;
    private int pointUser;

    // Constructor tanpa parameter dibutuhkan untuk Firebase
    public User() {
    }

    // Constructor dengan parameter
    public User(String nama, String tanggalBergabung, String email, String telpon, String tanggalLahir, int pointUser) {
        this.nama = nama;
        this.tanggalBergabung = tanggalBergabung;
        this.email = email;
        this.telpon = telpon;
        this.tanggalLahir = tanggalLahir;
        this.pointUser = pointUser;
    }

    // Getter dan Setter untuk setiap atribut
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

    public int getPointUser() {
        return pointUser;
    }

    public void setPointUser(int pointUser) {
        this.pointUser = pointUser;
    }
}
