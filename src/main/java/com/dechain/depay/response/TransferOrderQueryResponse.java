package com.dechain.depay.response;

import com.dechain.depay.model.TransferOrderQueryResModel;

/***
* DePay转账查单响应实现
*
* @author terrfly
* @site
* @date 2021/8/16 16:25
*/
public class TransferOrderQueryResponse extends DePayResponse {

    private static final long serialVersionUID = 7419683269497002904L;

    public TransferOrderQueryResModel get() {
        if(getData() == null) {
            return new TransferOrderQueryResModel();
        }
        return getData().toJavaObject(TransferOrderQueryResModel.class);
    }

    @Override
    public boolean isSuccess(String apiKey) {
        if(super.isSuccess(apiKey)) {
            int state = get().getState();
            return state == 0 || state == 1 || state == 2;
        }
        return false;
    }

}
