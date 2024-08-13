package loyality.member.cafe.boloessentials.model;

import java.util.Map;

public class Absen {
    public String namaKaryawan;
    public String tanggal;
    public String jam;
    public String hari;
    public boolean absen;
    public Long absensiKe; // New field

    public Absen() {
    }

    public Absen(Map<String, Object> absenMap) {
        this.hari = (String) absenMap.get("hari");
        this.tanggal = (String) absenMap.get("tanggal");
        this.jam = (String) absenMap.get("jam");
        this.absensiKe = (Long) absenMap.get("absensiKe"); // Initialize new field
    }

    public Absen(String namaKaryawan, String tanggal, String jam, String hari, boolean absen, Long absensiKe) {
        this.namaKaryawan = namaKaryawan;
        this.tanggal = tanggal;
        this.jam = jam;
        this.hari = hari;
        this.absen = absen;
        this.absensiKe = absensiKe;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public Boolean getAbsen() {
        return absen;
    }

    public void setAbsen(Boolean absen) {
        this.absen = absen;
    }

    public long getabsensiKe(Long absensiKe){
        return absensiKe;
    }

    public void setAbsensiKe(Long absensiKe){ this.absensiKe = absensiKe;}
}

