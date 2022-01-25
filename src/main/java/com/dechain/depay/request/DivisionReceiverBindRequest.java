package com.dechain.depay.request;

import com.dechain.depay.DePay;
import com.dechain.depay.model.DePayObject;
import com.dechain.depay.net.RequestOptions;
import com.dechain.depay.response.DivisionReceiverBindResponse;

/***
* 分账绑定接口
*
* @author terrfly
* @site
* @date 2021/8/25 10:34
*/
public class DivisionReceiverBindRequest implements DePayRequest<DivisionReceiverBindResponse> {

    private String apiVersion = DePay.VERSION;
    private String apiUri = "api/division/receiver/bind";
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
    public Class<DivisionReceiverBindResponse> getResponseClass() {
        return DivisionReceiverBindResponse.class;
    }

}
