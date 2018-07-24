package chinapex.com.wallet.adapter.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by SteelCabbage on 2018/7/24 0024 10:24.
 * E-Mail：liuyi_61@163.com
 */

public class PortraitPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
