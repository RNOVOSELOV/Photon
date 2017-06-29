package xyz.rnovoselov.photon.mvp.presenters;

import android.view.MenuItem;

/**
 * Created by roman on 26.01.17.
 */
public class MenuItemHolder {
    private final CharSequence itemTitle;
    private final int iconResId;
    private final MenuItem.OnMenuItemClickListener listener;

    public MenuItemHolder(CharSequence itemTitle, int iconResId, MenuItem.OnMenuItemClickListener listener) {
        this.itemTitle = itemTitle;
        this.iconResId = iconResId;
        this.listener = listener;
    }

    public CharSequence getTitle() {
        return itemTitle;
    }

    public int getIconResId() {
        return iconResId;
    }

    public MenuItem.OnMenuItemClickListener getListener() {
        return listener;
    }
}
