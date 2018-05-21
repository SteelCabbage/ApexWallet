package chinapex.com.wallet.view.assets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.AssetsRecyclerViewAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.ApexWalletApplication;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class AssetsFragment extends BaseFragment implements AssetsRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView mRv_assets;
    private ArrayList<WalletBean> mWalletBeans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {

        View fragment_assets = inflater.inflate(R.layout.fragment_assets, container, false);
        initView(fragment_assets);
        return fragment_assets;
    }

    @Override
    public void onStart() {
        super.onStart();

        mRv_assets.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance(), LinearLayoutManager.VERTICAL,
                false));
        AssetsRecyclerViewAdapter assetsRecyclerViewAdapter = new AssetsRecyclerViewAdapter(getData());
        assetsRecyclerViewAdapter.setOnItemClickListener(this);

        int space = 10;
        mRv_assets.addItemDecoration(new SpacesItemDecoration(space));

        mRv_assets.setAdapter(assetsRecyclerViewAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        mRv_assets = (RecyclerView) view.findViewById(R.id.rv_assets);
    }

    @Override
    public void onItemClick(int position) {

    }

    private List<WalletBean> getData() {
        mWalletBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WalletBean walletBean = new WalletBean();
            walletBean.setWalletName("walletName" + i);
            walletBean.setWalletAddr("kkkkksss123" + i);
            walletBean.setBalance(50 + i);
            mWalletBeans.add(walletBean);
        }
        return mWalletBeans;
    }


}
