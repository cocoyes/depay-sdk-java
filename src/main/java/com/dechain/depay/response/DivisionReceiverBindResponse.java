package com.dechain.depay.response;

import com.dechain.depay.model.DivisionReceiverBindResModel;


/***
* 分账账号的绑定响应实现
*
* @author terrfly
* @site
* @date 2021/8/25 10:35
*/
public class DivisionReceiverBindResponse extends DePayResponse {

    private static final long serialVersionUID = 7419683269497002904L;

    public DivisionReceiverBindResModel get() {
        if(getData() == null) return new DivisionReceiverBindResModel();
        return getData().toJavaObject(DivisionReceiverBindResModel.class);
    }

    @Override
    public boolean isSuccess(String apiKey) {
        if(super.isSuccess(apiKey)) {
            int state = get().getBindState();
            return state == 1;
        }
        return false;
    }

}
