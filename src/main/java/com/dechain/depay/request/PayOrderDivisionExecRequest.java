package com.dechain.depay.request;

import com.dechain.depay.DePay;
import com.dechain.depay.model.DePayObject;
import com.dechain.depay.net.RequestOptions;
import com.dechain.depay.response.PayOrderDivisionExecResponse;

/***
* 分账发起
*
* @author terrfly
* @site
* @date 2021/8/27 10:19
*/
public class PayOrderDivisionExecRequest implements DePayRequest<PayOrderDivisionExecResponse> {

    private String apiVersion = DePay.VERSION;
    private String apiUri = "api/division/exec";
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
    public Class<PayOrderDivisionExecResponse> getResponseClass() {
        return PayOrderDivisionExecResponse.class;
    }

}
