package com.dechain.depay;

import com.dechain.depay.exception.DePayException;
import com.dechain.depay.net.APIResource;
import com.dechain.depay.net.RequestOptions;
import com.dechain.depay.request.DePayRequest;
import com.dechain.depay.response.DePayResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * DePay sdk客户端
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public class DePayClient extends APIResource {

    private static Map<String, DePayClient> clientMap = new HashMap<String, DePayClient>();

    private String appId;
    private String signType = DePay.DEFAULT_SIGN_TYPE;
    private String apiKey = DePay.apiKey;
    private String apiBase = DePay.getApiBase();

    public String getAppId() {
        return appId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public DePayClient(String apiBase, String signType, String apiKey) {
        this.apiBase = apiBase;
        this.signType = signType;
        this.apiKey = apiKey;
    }

    public DePayClient(String apiBase, String apiKey) {
        this.apiBase = apiBase;
        this.apiKey = apiKey;
    }

    public DePayClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiBase() {
        return apiBase;
    }

    public void setApiBase(String apiBase) {
        this.apiBase = apiBase;
    }

    public DePayClient() {
    }

    public static synchronized DePayClient getInstance(String appId, String apiKey, String apiBase) {
        DePayClient client = clientMap.get(appId);
        if (client != null) {
            return client;
        }
        client = new DePayClient();
        clientMap.put(appId, client);
        client.appId = appId;
        client.apiKey = apiKey;
        client.apiBase = apiBase;
        return client;
    }

    public static synchronized DePayClient getInstance(String appId, String apiKey) {
        DePayClient client = clientMap.get(appId);
        if (client != null) {
            return client;
        }
        client = new DePayClient();
        clientMap.put(appId, client);
        client.appId = appId;
        client.apiKey = apiKey;
        return client;
    }

    public static synchronized DePayClient getInstance(String appId) {
        DePayClient client = clientMap.get(appId);
        if (client != null) {
            return client;
        }
        client = new DePayClient();
        clientMap.put(appId, client);
        client.appId = appId;
        return client;
    }

    public <T extends DePayResponse> T execute(DePayRequest<T> request) throws DePayException {

        // 支持用户自己设置RequestOptions
        if(request.getRequestOptions() == null) {
            RequestOptions options = RequestOptions.builder()
                    .setVersion(request.getApiVersion())
                    .setUri(request.getApiUri())
                    .setAppId(this.appId)
                    .setApiKey(this.apiKey)
                    .build();
            request.setRequestOptions(options);
        }

        return execute(request, RequestMethod.POST, this.apiBase);
    }

}
