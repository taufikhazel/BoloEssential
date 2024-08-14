package loyality.member.cafe.boloessentials.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.model.Menu;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<Menu> menuList;
    private List<Menu> selectedMenus = new ArrayList<>(); // List to keep track of selected items

    public MenuAdapter(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);

        holder.menuTitle.setText(menu.getNamaMenu());
        holder.menuPoints.setText(menu.getPoint() + " Point");

        Picasso.get()
                .load(menu.getGambar())
                .into(holder.menuImage);

        // Highlight the selected item
        holder.itemView.setBackgroundColor(selectedMenus.contains(menu) ? context.getResources().getColor(R.color.selectedColor) : context.getResources().getColor(android.R.color.transparent));

        holder.itemView.setOnClickListener(v -> {
            if (selectedMenus.contains(menu)) {
                selectedMenus.remove(menu); // Deselect
            } else {
                selectedMenus.add(menu); // Select
            }
            notifyDataSetChanged(); // Refresh the item view to show selection
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public List<Menu> getSelectedMenus() {
        return selectedMenus;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView menuTitle, menuPoints;
        ImageView menuImage;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuTitle = itemView.findViewById(R.id.menuTitle);
            menuPoints = itemView.findViewById(R.id.menuPoints);
            menuImage = itemView.findViewById(R.id.menuImage);
        }
    }
}
