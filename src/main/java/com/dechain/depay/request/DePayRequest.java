package com.dechain.depay.request;

import com.dechain.depay.model.DePayObject;
import com.dechain.depay.net.RequestOptions;
import com.dechain.depay.response.DePayResponse;

/**
 * DePay请求接口
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public interface DePayRequest<T extends DePayResponse> {

    /**
     * 获取当前接口的路径
     * @return
     */
    String getApiUri();

    /**
     * 获取当前接口的版本
     * @return
     */
    String getApiVersion();

    /**
     * 设置当前接口的版本
     * @return
     */
    void setApiVersion(String apiVersion);

    RequestOptions getRequestOptions();

    void setRequestOptions(RequestOptions options);

    DePayObject getBizModel();

    void setBizModel(DePayObject bizModel);

    Class<T> getResponseClass();

}
