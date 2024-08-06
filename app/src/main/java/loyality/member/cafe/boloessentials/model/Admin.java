package loyality.member.cafe.boloessentials.model;

public class Admin {
        private String nama;
        private String email;
        private String nomorID;
        private String notelp;

        // Constructor tanpa parameter dibutuhkan untuk Firebase
        public Admin() {
        }

        // Constructor dengan parameter
        public Admin(String nama, String email, String nomorID, String notelp) {
            this.nama = nama;
            this.email = email;
            this.nomorID = nomorID;
            this.notelp = notelp;
        }

        // Getter dan Setter untuk setiap atribut
        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getnomorID() {
            return nomorID;
        }

        public void setnomorID(String nomorID) {
            this.nomorID = nomorID;
        }

        public String getTelpon() {
            return notelp;
        }

        public void setTelpon(String notelp) {
            this.notelp = notelp;
        }
}
