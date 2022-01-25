package com.dechain.depay.response;

import com.dechain.depay.model.RefundOrderQueryResModel;

/**
 * DePay退款查单响应实现
 * @author apachemoy
 * @site
 * @date 2021-06-18 12:00
 */
public class RefundOrderQueryResponse extends DePayResponse {

    private static final long serialVersionUID = 7654172640802954221L;

    public RefundOrderQueryResModel get() {
        if(getData() == null) return new RefundOrderQueryResModel();
        return getData().toJavaObject(RefundOrderQueryResModel.class);
    }

}
