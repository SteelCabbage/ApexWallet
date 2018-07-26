package chinapex.com.wallet.view.me.portrait;

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
import chinapex.com.wallet.adapter.PortraitRecyclerViewAdapter;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.PortraitBean;
import chinapex.com.wallet.bean.PortraitTagsBean;
import chinapex.com.wallet.bean.json.PortraitZh;
import chinapex.com.wallet.bean.response.ResponsePortrait;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.GsonUtils;

/**
 * Created by SteelCabbage on 2018/7/24 0024 15:09.
 * E-Mail：liuyi_61@163.com
 */

public class MeCommonPortraitFragment extends BaseFragment implements PortraitRecyclerViewAdapter
        .OnItemClickListener {
    private static final String TAG = MeCommonPortraitFragment.class.getSimpleName();
    private RecyclerView mRv_portrait_common;
    private List<PortraitBean> mPortraitBeans;
    private PortraitRecyclerViewAdapter mPortraitRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_common, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }

    private void initData() {
        mPortraitBeans = new ArrayList<>();
        ResponsePortrait responsePortrait = GsonUtils.json2Bean(PortraitZh.PORTRAIT_ZH_CN,
                ResponsePortrait.class);
        if (null == responsePortrait) {
            CpLog.e(TAG, "responsePortrait is null!");
            return;
        }

        List<ResponsePortrait.ResultBean> result = responsePortrait.getResult();
        if (null == result || result.isEmpty()) {
            CpLog.e(TAG, "result is null or empty!");
            return;
        }

        for (ResponsePortrait.ResultBean resultBean : result) {
            PortraitBean portraitBean = new PortraitBean();
            portraitBean.setType(resultBean.getType());
            portraitBean.setTitle(resultBean.getTitle());
            portraitBean.setResource(resultBean.getResource());
            List<ResponsePortrait.ResultBean.DataBean> data = resultBean.getData();
            List<PortraitTagsBean> portraitTagsBeans = new ArrayList<>();
            for (ResponsePortrait.ResultBean.DataBean datum : data) {
                PortraitTagsBean portraitTagsBean = new PortraitTagsBean();
                portraitTagsBean.setName(datum.getName());
                portraitTagsBean.setId(datum.getId());
                portraitTagsBeans.add(portraitTagsBean);
            }
            portraitBean.setData(portraitTagsBeans);
            mPortraitBeans.add(portraitBean);
        }

    }

    private void initView(View view) {
        mRv_portrait_common = view.findViewById(R.id.rv_portrait_common);
        mRv_portrait_common.setLayoutManager(new LinearLayoutManager(ApexWalletApplication
                .getInstance(),
                LinearLayoutManager.VERTICAL, false));
        mPortraitRecyclerViewAdapter = new PortraitRecyclerViewAdapter(mPortraitBeans);
        mPortraitRecyclerViewAdapter.setOnItemClickListener(this);
        mRv_portrait_common.setAdapter(mPortraitRecyclerViewAdapter);
    }


    @Override
    public void onItemClick(int position) {

    }
}
