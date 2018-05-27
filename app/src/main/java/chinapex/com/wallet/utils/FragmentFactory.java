package chinapex.com.wallet.utils;

import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.view.assets.AssetsFragment;
import chinapex.com.wallet.view.discover.DiscoverFragment;
import chinapex.com.wallet.view.me.MeFragment;

public class FragmentFactory {

    private static DiscoverFragment sDiscoverFragment;
    private static AssetsFragment sAssetsFragment;
    private static MeFragment sMeFragment;

    public static BaseFragment getFragment(int position) {
        BaseFragment baseFragment = null;
        switch (position) {
            case 0:
                if (null == sDiscoverFragment) {
                    sDiscoverFragment = new DiscoverFragment();
                }
                baseFragment = sDiscoverFragment;
                break;
            case 1:
                if (null == sAssetsFragment) {
                    sAssetsFragment = new AssetsFragment();
                }
                baseFragment = sAssetsFragment;
                break;
            case 2:
                if (null == sMeFragment) {
                    sMeFragment = new MeFragment();
                }
                baseFragment = sMeFragment;
                break;
            case 3:
                //home page reserved
                break;
            case 4:
                //home page reserved
                break;
            case 5:
                //home page reserved
                break;
            case 6:
                //home page reserved
                break;

            //backup page
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
        }
        return baseFragment;
    }
}
