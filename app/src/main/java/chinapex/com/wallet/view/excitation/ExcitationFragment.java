package chinapex.com.wallet.view.excitation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.ExcitationAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecorationBottom;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.ExcitationBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.utils.DensityUtil;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class ExcitationFragment extends BaseFragment implements ExcitationAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnScrollChangeListener {

    private static final String TAG = ExcitationFragment.class.getSimpleName();

    private List<ExcitationBean> mList;
    private RecyclerView mExcitationEvnet;
    private RelativeLayout mExcitationApexHeader;
    private RelativeLayout mExcitationHeader;
    private SwipeRefreshLayout mExcitationRefresh;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {

        return inflater.inflate(R.layout.fragment_excitation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }

    private void initView(View view) {
        mExcitationApexHeader = (RelativeLayout) view.findViewById(R.id.excitation_apex_header);
        mExcitationHeader = (RelativeLayout) view.findViewById(R.id.excitation_header);
        mExcitationEvnet = (RecyclerView) view.findViewById(R.id.new_event);
        mExcitationRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_event_refresh);

        mExcitationEvnet.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ExcitationAdapter adapter = new ExcitationAdapter(mList);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_excitation_recyclerview_item_header, mExcitationEvnet, false);
        adapter.addHeaderView(header);
        adapter.setOnItemClickListener(this);
        mExcitationEvnet.setAdapter(adapter);
        mExcitationEvnet.setOnScrollChangeListener(this);

        int space = DensityUtil.dip2px(getActivity(), 15);
        mExcitationEvnet.addItemDecoration(new SpacesItemDecorationBottom(space));
        mExcitationRefresh.setColorSchemeColors(ApexWalletApplication.getInstance().getResources().getColor(R.color.c_1253BF));
        mExcitationRefresh.setOnRefreshListener(this);
       /* mExcitationRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                if (mExcitationEvnet == null) {
                    return false;
                }
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mExcitationEvnet.getLayoutManager();
                return linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0;
            }
        });*/

    }

    private void initData() {
        mList = new ArrayList<>();
        //For Test start

        for (int i = 0; i < 3; i++) {
            ExcitationBean bean = new ExcitationBean();
            bean.setEventNew(true);
            bean.setNewEventStatus(1);
            bean.setNewEventPic("E:\\Project\\ApexWallet\\app\\src\\main\\res\\mipmap-hdpi");
            bean.setNewEventText("UN红日UC隔日个人才是与停车让分析调查VB怒不予通过课子级用户提供如此发型非常GV红包君就不会v");
            mList.add(bean);
        }

        //For Test End
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ExcitationDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
         mExcitationRefresh.setRefreshing(false);
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        if (null == view) return;
        RecyclerView.LayoutManager layoutManager = mExcitationEvnet.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            if (firstItemPosition != 0) {
                mExcitationApexHeader.setVisibility(View.INVISIBLE);
            } else {
                mExcitationApexHeader.setVisibility(View.VISIBLE);
            }
        }
    }
}
