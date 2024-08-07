package loyality.member.cafe.boloessentials.model;

public class Menu {
    private String Gambar;
    private String NamaMenu;
    private int Point;
    private boolean Show;
    private String key; // Add this line

    // Constructor without parameters needed for Firebase
    public Menu() {
    }

    // Constructor with parameters
    public Menu(String Gambar, String NamaMenu, int Point, boolean Show) {
        this.Gambar = Gambar;
        this.NamaMenu = NamaMenu;
        this.Point = Point;
        this.Show = Show;
    }

    // Getter and Setter for each attribute
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

    // Getter and Setter for key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
