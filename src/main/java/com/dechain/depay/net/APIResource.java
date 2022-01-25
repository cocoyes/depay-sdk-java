package com.dechain.depay.net;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.dechain.depay.exception.APIException;
import com.dechain.depay.exception.InvalidRequestException;
import com.dechain.depay.exception.DePayException;
import com.dechain.depay.request.DePayRequest;
import com.dechain.depay.response.DePayResponse;
import com.dechain.depay.util.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * API资源抽象类
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public abstract class APIResource  {

    private static final Logger _log = LoggerFactory.getLogger(APIResource.class);

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private static HttpClient httpClient = new HttpURLConnectionClient();

    protected enum RequestMethod {
        GET,
        POST,
        DELETE,
        PUT
    }

    public static Class<?> getSelfClass() {
        return APIResource.class;
    }

    protected static String urlEncode(String str) {
        if (str == null) {
            return null;
        }

        try {
            return URLEncoder.encode(str, CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unknown");
        }
    }

    public <T extends DePayResponse> T execute(
            DePayRequest<T> request,
            RequestMethod method,
            String url) throws DePayException {

        String jsonParam = new JSONWriter().write(request.getBizModel(), true);

        JSONObject params = JSONObject.parseObject(jsonParam);
        request.getRequestOptions();
        APIDePayRequest apiDePayRequest = new APIDePayRequest(method, url, params, request.getRequestOptions());
        if(_log.isDebugEnabled()) _log.debug("DePay_SDK_REQ：url={}, data={}", apiDePayRequest.getUrl(), JSONObject.toJSONString(apiDePayRequest.getParams()));
        APIDePayResponse response = httpClient.requestWithRetries(apiDePayRequest);
        int responseCode = response.getResponseCode();
        String responseBody = response.getResponseBody();
        if(_log.isDebugEnabled()) _log.debug("DePay_SDK_RES：code={}, body={}", responseCode, responseBody);
        if (responseCode != 200) {
            handleAPIError(response);
        }

        T resource = null;

        try {
            resource = JSONObject.parseObject(responseBody, request.getResponseClass());
        } catch (JSONException e) {
            raiseMalformedJsonError(responseBody, responseCode);
        }

        return resource;
    }

    /**
     * 错误处理
     * @param response
     * @throws DePayException
     */
    private static void handleAPIError(APIDePayResponse response)
            throws DePayException {

        String rBody = response.getResponseBody();
        int rCode = response.getResponseCode();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(rBody);

        } catch (JSONException e) {
            raiseMalformedJsonError(rBody, rCode);
        }

        if(rCode == 404) {
            throw new InvalidRequestException(jsonObject.getString("status") + ", "
                    + jsonObject.getString("error") + ", "
                    + jsonObject.getString("path")
                    , rCode, null);
        }

    }

    private static void raiseMalformedJsonError(
            String responseBody, int responseCode) throws APIException {
        throw new APIException(
                String.format(
                        "Invalid response object from API: %s. (HTTP response code was %d)",
                        responseBody, responseCode),
                null,
                null,
                responseCode,
                null);
    }
}
