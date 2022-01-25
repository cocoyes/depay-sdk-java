package com.dechain.depay.response;

import com.dechain.depay.model.PayOrderQueryResModel;

/**
 * DePay支付查单响应实现
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public class PayOrderQueryResponse extends DePayResponse {

    private static final long serialVersionUID = 7654172640802954221L;

    public PayOrderQueryResModel get() {
        if(getData() == null) return new PayOrderQueryResModel();
        return getData().toJavaObject(PayOrderQueryResModel.class);
    }

}
