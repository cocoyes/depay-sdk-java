package com.dechain.depay.request;

import com.dechain.depay.DePay;
import com.dechain.depay.model.DePayObject;
import com.dechain.depay.net.RequestOptions;
import com.dechain.depay.response.RefundOrderCreateResponse;

/**
 * DePay退款请求实现
 * @author apachemoy
 * @site
 * @date 2021-06-18 09:00
 */
public class RefundOrderCreateRequest implements DePayRequest<RefundOrderCreateResponse> {

    private String apiVersion = DePay.VERSION;
    private String apiUri = "api/refund/refundOrder";
    private RequestOptions options;
    private DePayObject bizModel = null;

    @Override
    public String getApiUri() {
        return this.apiUri;
    }

    @Override
    public String getApiVersion() {
        return this.apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public RequestOptions getRequestOptions() {
        return this.options;
    }

    @Override
    public void setRequestOptions(RequestOptions options) {
        this.options = options;
    }

    @Override
    public DePayObject getBizModel() {
        return this.bizModel;
    }

    @Override
    public void setBizModel(DePayObject bizModel) {
        this.bizModel = bizModel;
    }

    @Override
    public Class<RefundOrderCreateResponse> getResponseClass() {
        return RefundOrderCreateResponse.class;
    }

}
