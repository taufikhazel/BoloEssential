package loyality.member.cafe.boloessentials.model;

public class Menu {
    private String Gambar;
    private String NamaMenu;
    private int Point;
    private boolean Show; // Ubah dari String ke boolean

    // Constructor tanpa parameter dibutuhkan untuk Firebase
    public Menu() {
    }

    // Constructor dengan parameter
    public Menu(String Gambar, String NamaMenu, int Point, boolean Show) {
        this.Gambar = Gambar;
        this.NamaMenu = NamaMenu;
        this.Point = Point;
        this.Show = Show;
    }

    // Getter dan Setter untuk setiap atribut
    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String Gambar) {
        this.Gambar = Gambar;
    }

    public String getNamaMenu() {
        return NamaMenu;
    }

    public void setNamaMenu(String NamaMenu) {
        this.NamaMenu = NamaMenu;
    }

    public boolean getShow() {
        return Show;
    }

    public void setShow(boolean Show) {
        this.Show = Show;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int Point) {
        this.Point = Point;
    }
}
