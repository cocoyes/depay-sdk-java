package com.dechain.depay.net;

import com.dechain.depay.exception.APIConnectionException;
import com.dechain.depay.util.StreamUtils;
import com.dechain.depay.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpURL连接客户端
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public class HttpURLConnectionClient extends HttpClient {

    public HttpURLConnectionClient() {
        super();
    }

    @Override
    public APIDePayResponse request(APIDePayRequest request) throws APIConnectionException {
        HttpURLConnection conn = null;

        try {
            conn = createDePayConnection(request);

            // trigger the request
            int responseCode = conn.getResponseCode();
            HttpHeaders headers = HttpHeaders.of(conn.getHeaderFields());
            String responseBody;

            if (responseCode >= 200 && responseCode < 300) {
                responseBody = StreamUtils.readToEnd(conn.getInputStream(), APIResource.CHARSET);
            } else {
                responseBody = StreamUtils.readToEnd(conn.getErrorStream(), APIResource.CHARSET);
            }

            return new APIDePayResponse(responseCode, responseBody, headers);
        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format(
                            "请求DePay(%s)异常,请检查网络或重试.异常信息:%s",
                            request.getUrl(), e.getMessage()),
                    e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    static HttpHeaders getHeaders(APIDePayRequest request) {
        Map<String, List<String>> userAgentHeadersMap = new HashMap<>();

        userAgentHeadersMap.put("User-Agent", Collections.singletonList(buildUserAgentString(request.getOptions().getVersion())));
        userAgentHeadersMap.put(
                "X-DePay-Client-User-Agent", Collections.singletonList(buildXDePayClientUserAgentString(request.getOptions().getVersion())));

        return request.getHeaders().withAdditionalHeaders(userAgentHeadersMap);
    }

    private static HttpURLConnection createDePayConnection(APIDePayRequest request)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) request.url.openConnection();

        conn.setConnectTimeout(request.options.getConnectTimeout());
        conn.setReadTimeout(request.options.getReadTimeout());
        conn.setUseCaches(false);
        for (Map.Entry<String, List<String>> entry : getHeaders(request).map().entrySet()) {
            conn.setRequestProperty(entry.getKey(), StringUtils.join(",", entry.getValue()));
        }

        conn.setRequestMethod(request.method.name());

        // 如有其他业务参数，可在此处增加

        if (request.content != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", request.content.contentType);

            try (OutputStream output = conn.getOutputStream()) {
                output.write(request.content.byteArrayContent);
            }
        }

        return conn;
    }
}
