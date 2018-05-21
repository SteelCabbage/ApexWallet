package chinapex.com.wallet.view.me;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chinapex.com.wallet.R;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class MeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {

        View fragment_market = inflater.inflate(R.layout.fragment_me, container, false);
        return fragment_market;
    }
}
