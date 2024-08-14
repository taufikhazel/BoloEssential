package loyality.member.cafe.boloessentials.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import loyality.member.cafe.boloessentials.model.Menu;
import loyality.member.cafe.boloessentials.halaman_userandworker.MenuFragment;

public class MenuPagerAdapter extends FragmentStateAdapter {

    private List<Menu> menuList;
    private int itemsPerPage;
    private List<MenuFragment> fragmentList = new ArrayList<>();

    public MenuPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Menu> menuList, int itemsPerPage) {
        super(fragmentActivity);
        this.menuList = menuList;
        this.itemsPerPage = itemsPerPage;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int startPosition = position * itemsPerPage;
        int endPosition = Math.min(startPosition + itemsPerPage, menuList.size());

        MenuFragment fragment = MenuFragment.newInstance(menuList.subList(startPosition, endPosition));
        if (fragmentList.size() <= position) {
            fragmentList.add(fragment);
        } else {
            fragmentList.set(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) menuList.size() / itemsPerPage);
    }

    public MenuFragment getFragment(int position) {
        return (position < fragmentList.size()) ? fragmentList.get(position) : null;
    }
}
