package chinapex.com.wallet.view.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseFragment;

/**
 * Created by SteelCabbage on 2018/7/23 0023 18:44.
 * E-Mail：liuyi_61@163.com
 */

public class MePortraitFragment extends BaseFragment {
    private static final String TAG = MePortraitFragment.class.getSimpleName();
    private TabLayout mTl_portrait;
    private ViewPager mVp_portrait;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_portrait, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mTl_portrait = view.findViewById(R.id.tl_portrait);
        mVp_portrait = view.findViewById(R.id.vp_portrait);
    }

    private void initData() {
        mTl_portrait.setupWithViewPager(mVp_portrait);
    }
}
