package loyality.member.cafe.boloessentials.model;

public class Karyawan {
        private String namaKaryawan;
        private String tanggalBergabung;
        private String emailKaryawan;
        private String telponKaryawan;
        private String tanggalLahir;

        // Constructor tanpa parameter dibutuhkan untuk Firebase
        public Karyawan() {
        }

        // Constructor dengan parameter
        public Karyawan(String namaKaryawan, String tanggalBergabung, String emailKaryawan, String telponKaryawan, String tanggalLahir, int pointUser) {
            this.namaKaryawan = namaKaryawan;
            this.tanggalBergabung = tanggalBergabung;
            this.emailKaryawan = emailKaryawan;
            this.telponKaryawan = telponKaryawan;
            this.tanggalLahir = tanggalLahir;
        }

        // Getter dan Setter untuk setiap atribut
        public String getNama() {
            return namaKaryawan;
        }

        public void setNama(String namaKaryawan) {
            this.namaKaryawan = namaKaryawan;
        }

        public String getTanggalBergabung() {
            return tanggalBergabung;
        }

        public void setTanggalBergabung(String tanggalBergabung) {
            this.tanggalBergabung = tanggalBergabung;
        }

        public String getEmail() {
            return emailKaryawan;
        }

        public void setEmail(String emailKaryawan) {
            this.emailKaryawan = emailKaryawan;
        }

        public String getTelpon() {
            return telponKaryawan;
        }

        public void setTelpon(String telponKaryawan) {
            this.telponKaryawan = telponKaryawan;
        }

        public String getTanggalLahir() {
            return tanggalLahir;
        }

        public void setTanggalLahir(String tanggalLahir) {
            this.tanggalLahir = tanggalLahir;
        }
}
