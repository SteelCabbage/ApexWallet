package chinapex.com.wallet.view.me.portrait;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseFragment;

/**
 * Created by SteelCabbage on 2018/7/24 0024 15:09.
 * E-Mail：liuyi_61@163.com
 */

public class MeCommonPortraitFragment extends BaseFragment {
    private static final String TAG = MeCommonPortraitFragment.class.getSimpleName();
    private RecyclerView mRv_portrait_common;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_common, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mRv_portrait_common = view.findViewById(R.id.rv_portrait_common);
    }

    private void initData() {

    }
}
