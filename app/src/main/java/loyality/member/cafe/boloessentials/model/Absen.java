package loyality.member.cafe.boloessentials.model;

public class Absen {
    public String UID;
    public String nama;

    public String tanggal;
    public String jam;
    public String hari;
    public boolean absen;


    public Absen() {
    }

    public Absen(String UID, String nama, String tanggal, String jam, String hari, boolean absen) {
        this.UID = UID;
        this.nama = nama;
        this.tanggal = tanggal;
        this.jam = jam;
        this.hari = hari;
        this.absen = absen;
    }
}

