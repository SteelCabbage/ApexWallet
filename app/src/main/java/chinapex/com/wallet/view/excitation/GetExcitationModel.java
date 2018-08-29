package chinapex.com.wallet.view.excitation;

import java.util.List;

import chinapex.com.wallet.bean.ExcitationBean;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;

public class GetExcitationModel implements IGetExcitationModel, IGetExcitationCallback {
    private static final String TAG = GetExcitationModel.class.getSimpleName();
    private IGetExcitationModelCallback mIGetExcitationModelCallback;

    public GetExcitationModel(IGetExcitationModelCallback mIGetExcitationModelCallback) {
        this.mIGetExcitationModelCallback = mIGetExcitationModelCallback;
    }

    @Override
    public void getExcitation() {
        TaskController.getInstance().submit(new GetExcitation(Constant.EXCITATION_SHOW_LIST, this));
    }

    @Override
    public void getExcitation(List<ExcitationBean> excitationBeans) {
        if (null == excitationBeans) {
            CpLog.i(TAG, "excitationBeans is null");
        }
        mIGetExcitationModelCallback.getExcitation(excitationBeans);
    }
}
