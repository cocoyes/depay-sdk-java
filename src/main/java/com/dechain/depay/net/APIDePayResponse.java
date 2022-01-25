package com.dechain.depay.net;

/**
 * API响应
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public class APIDePayResponse {

    private int responseCode;
    private String responseBody;
    private HttpHeaders responseHeaders;

    private int numRetries;

    public APIDePayResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseHeaders = null;
    }

    public APIDePayResponse(int responseCode, String responseBody, HttpHeaders responseHeaders) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseHeaders = responseHeaders;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(int numRetries) {
        this.numRetries = numRetries;
    }
}
