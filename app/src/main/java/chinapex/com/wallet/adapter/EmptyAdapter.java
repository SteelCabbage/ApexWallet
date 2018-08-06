package chinapex.com.wallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by SteelCabbage on 2018/8/6 0006 17:36.
 * E-Mail：liuyi_61@163.com
 */

public class EmptyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = EmptyAdapter.class.getSimpleName();
    private RecyclerView.Adapter mAdapter; //需要装饰的Adapter
    private Context mContext;
    private final int EMPTY_VIEW = 0;
    private final int NOT_EMPTY_VIEW = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
