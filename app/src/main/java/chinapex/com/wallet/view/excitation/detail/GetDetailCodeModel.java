package chinapex.com.wallet.view.excitation.detail;

import java.util.List;

import chinapex.com.wallet.bean.AddressResultCode;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;

public class GetDetailCodeModel implements IDetailCodeModel, IGetDetailCodeCallback {
    private static final String TAG = GetDetailCodeModel.class.getSimpleName();
    IGetResultCodeModelCallback mIGetResultCodeModelCallback;

    public GetDetailCodeModel(IGetResultCodeModelCallback iGetResultCodeModelCallback) {
        mIGetResultCodeModelCallback = iGetResultCodeModelCallback;
    }

    @Override
     public void getDetailCode(List<String> addressList) {
        TaskController.getInstance().submit(new GetDetailCode(Constant.EXCITATION_DETAIL_UPLOAD_ADDRESS, addressList, this));
    }


    @Override
    public void getDetailCode(AddressResultCode addressResultCode) {
        mIGetResultCodeModelCallback.getResultCode(addressResultCode);
    }
}
