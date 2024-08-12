package loyality.member.cafe.boloessentials.halaman_userandworker;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import loyality.member.cafe.boloessentials.R;
import loyality.member.cafe.boloessentials.adapter.MenuAdapter;
import loyality.member.cafe.boloessentials.model.Menu;

public class MenuFragment extends Fragment {

    private static final String ARG_MENU_LIST = "menu_list";
    private List<Menu> menuList;

    public static MenuFragment newInstance(List<Menu> menuList) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MENU_LIST, new ArrayList<>(menuList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        if (getArguments() != null) {
            menuList = getArguments().getParcelableArrayList(ARG_MENU_LIST);
        }

        RecyclerView recyclerViewMenu = view.findViewById(R.id.recyclerViewMenu);
        recyclerViewMenu.setLayoutManager(new GridLayoutManager(getContext(), 5)); // 5 columns per page
        MenuAdapter menuAdapter = new MenuAdapter(getContext(), menuList);
        recyclerViewMenu.setAdapter(menuAdapter);

        return view;
    }
}
