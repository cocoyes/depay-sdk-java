package com.dechain.depay.net;

import com.dechain.depay.DePay;
import com.dechain.depay.exception.APIConnectionException;
import com.dechain.depay.exception.DePayException;
import com.dechain.depay.util.DePayKit;
import com.dechain.depay.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * API请求
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public class APIDePayRequest {
    /**
     * 请求方法 (GET, POST, DELETE or PUT)
     * */
    APIResource.RequestMethod method;

    /**
     * 请求URL
     */
    URL url;

    /**
     * 请求Body
     */
    HttpContent content;

    /**
     * 请求Header
     */
    HttpHeaders headers;

    /**
     * 请求参数
     */
    Map<String, Object> params;

    /**
     * 请求选项
     */
    RequestOptions options;

    /**
     * 实例化DePay请求
     * @param method
     * @param url
     * @param params
     * @param options
     * @throws DePayException
     */
    public APIDePayRequest(
            APIResource.RequestMethod method,
            String url,
            Map<String, Object> params,
            RequestOptions options)
            throws DePayException {
        try {
            this.params = (params != null) ? Collections.unmodifiableMap(params) : null;
            this.options = options;
            this.method = method;
            this.url = buildURL(method, StringUtils.genUrl(url, this.options.getUri()), params);
            this.content = buildContent(method, params, this.options);
            this.headers = buildHeaders(method, this.options);
        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format(
                            "请求DePay(%s)异常,请检查网络或重试.异常信息:%s",
                            StringUtils.genUrl(url, options.getUri()), e.getMessage()),
                    e);
        }
    }

    private static URL buildURL(
            APIResource.RequestMethod method, String spec, Map<String, Object> params)
            throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(spec);

        if ((method != APIResource.RequestMethod.POST && method != APIResource.RequestMethod.PUT) && (params != null)) {
            String queryString = createQuery(params);
            if (!queryString.isEmpty()) {
                sb.append("?");
                sb.append(queryString);
            }
        }

        return new URL(sb.toString());
    }

    private static HttpContent buildContent (
            APIResource.RequestMethod method, Map<String, Object> params, RequestOptions options) throws DePayException {
        if (method != APIResource.RequestMethod.POST && method != APIResource.RequestMethod.PUT) {
            return null;
        }

        if (params == null) {
            return null;
        }

        params.put(DePay.API_VERSION_NAME, options.getVersion());
        params.put(DePay.API_SIGN_TYPE_NAME, options.getSignType());
        String requestTime = currentTimeString();
        params.put(DePay.API_REQ_TIME_NAME, requestTime);
        String signature;
        try {
            signature = buildDePaySignature(params, options);
        } catch (IOException e) {
            throw new APIConnectionException("生成DePay请求签名异常", e);
        }
        if (signature != null) {
            params.put(DePay.API_SIGN_NAME, signature);
        }

        return HttpContent.buildJSONContent(params);
    }

    /**
     * @param params the parameters
     * @return queryString
     */
    private static String createQuery(Map<String, Object> params) {
        if (params == null) {
            return "";
        }

        Map<String, String> flatParams = flattenParams(params);
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : flatParams.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(),
                    entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    /**
     * @param k the key
     * @param v the value
     * @return urlEncodedString
     */
    private static String urlEncodePair(String k, String v) {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    /**
     * @param str the string to encode
     * @return urlEncodedString
     */
    protected static String urlEncode(String str) {
        if (str == null) {
            return null;
        }

        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unknown");
        }
    }

    /**
     * @param params the parameters
     * @return flattenParams
     */
    private static Map<String, String> flattenParams(Map<String, Object> params) {
        if (params == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> flatParams = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                Map<?, ?> nestedMap = (Map<?, ?>) value;
                for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
                    flatNestedMap.put(
                            String.format("%s[%s]", key, nestedEntry.getKey()),
                            nestedEntry.getValue());
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value instanceof ArrayList<?>) {
                ArrayList<?> ar = (ArrayList<?>) value;
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                int size = ar.size();
                for (int i = 0; i < size; i++) {
                    flatNestedMap.put(String.format("%s[%d]", key, i), ar.get(i));
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value == null) {
                flatParams.put(key, "");
            } else {
                flatParams.put(key, value.toString());
            }
        }
        return flatParams;
    }

    private static HttpHeaders buildHeaders(APIResource.RequestMethod method, RequestOptions options)
            throws DePayException {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>();

        // Accept
        headerMap.put("Accept", Collections.singletonList("application/json"));

        // Accept-Charset
        headerMap.put("Accept-Charset", Collections.singletonList(APIResource.CHARSET.name()));

        // Accept-Language
        headerMap.put("Accept-Language", Collections.singletonList(options.getAcceptLanguage()));

        return HttpHeaders.of(headerMap);
    }

    protected static String buildDePaySignature(Map<String, Object> params, RequestOptions options)
            throws IOException {

        String signType = options.getSignType();
        if("MD5".equalsIgnoreCase(signType)) {
            return DePayKit.getSign(params, options.getApiKey());
        }else if("RSA2".equalsIgnoreCase(signType)) {
            throw new AssertionError("暂不支持RSA2签名");
        }
        throw new AssertionError("请设置正确的签名类型");
    }

    protected static String currentTimeString() {
        int requestTime = (int) (System.currentTimeMillis() / 1000);
        return Integer.toString(requestTime);
    }

    public APIResource.RequestMethod getMethod() {
        return method;
    }

    public URL getUrl() {
        return url;
    }

    public HttpContent getContent() {
        return content;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public RequestOptions getOptions() {
        return options;
    }
}
