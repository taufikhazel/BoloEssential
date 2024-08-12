package loyality.member.cafe.boloessentials.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {
    private String Gambar;
    private String NamaMenu;
    private int Point;
    private boolean Show;
    private int IDMenu;

    // Constructor without parameters needed for Firebase
    public Menu() {
    }

    // Constructor with parameters
    public Menu(String Gambar, String NamaMenu, int Point, boolean Show, int IDMenu) {
        this.Gambar = Gambar;
        this.NamaMenu = NamaMenu;
        this.Point = Point;
        this.Show = Show;
        this.IDMenu = IDMenu;
    }

    protected Menu(Parcel in) {
        Gambar = in.readString();
        NamaMenu = in.readString();
        Point = in.readInt();
        Show = in.readByte() != 0;
        IDMenu = in.readInt();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Gambar);
        dest.writeString(NamaMenu);
        dest.writeInt(Point);
        dest.writeByte((byte) (Show ? 1 : 0));
        dest.writeInt(IDMenu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters
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

    public int getPoint() {
        return Point;
    }

    public void setPoint(int Point) {
        this.Point = Point;
    }

    public boolean getShow() {
        return Show;
    }

    public void setShow(boolean Show) {
        this.Show = Show;
    }

    public int getIDMenu() {
        return IDMenu;
    }

    public void setIDMenu(int IDMenu) {
        this.IDMenu = IDMenu;
    }
}
