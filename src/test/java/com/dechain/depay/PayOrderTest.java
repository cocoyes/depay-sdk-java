package com.dechain.depay;

import com.alibaba.fastjson.JSONObject;
import com.dechain.depay.exception.DePayException;
import com.dechain.depay.model.PayOrderCreateReqModel;
import com.dechain.depay.model.PayOrderQueryReqModel;
import com.dechain.depay.request.PayOrderCreateRequest;
import com.dechain.depay.request.PayOrderQueryRequest;
import com.dechain.depay.response.PayOrderCreateResponse;
import com.dechain.depay.response.PayOrderQueryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

class PayOrderTest {

    final static Logger _log = LoggerFactory.getLogger(PayOrderTest.class);

    @BeforeAll
    public static void initApiKey() {
        DePay.setApiBase(DepayTestData.getApiBase());
        DePay.apiKey = DepayTestData.getApiKey();
        DePay.mchNo = DepayTestData.getMchNo();
        DePay.appId = DepayTestData.getAppId();
    }

    @Test
    public void testPayOrderCreate() {

        /*
            支持自己定义RequestOptions属性,更灵活
            RequestOptions options = RequestOptions.builder().setAppId("60deb8d6c6104c854e2346e4").setApiKey("11982212000912313").setUri("api/pay/unifiedOrder").setReadTimeout(100).build();
            PayOrderCreateRequest request = new PayOrderCreateRequest();
            request.setRequestOptions(options);
        */

        /*
            特殊支付方式：
            QR_CASHIER  ( 通过二维码跳转到收银台完成支付， 已集成获取用户ID的实现。  )
            AUTO_BAR （自动分类条码支付）
        */

        // 支付接口文档：https://docs.jeequan.com/docs/DePay/payment_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        String wayCode = "CHAIN_APP";                           // 支付方式
        PayOrderCreateRequest request = new PayOrderCreateRequest();
        PayOrderCreateReqModel model = new PayOrderCreateReqModel();
        model.setMchNo(DePay.mchNo);                       // 商户号
        model.setAppId(DePayClient.getAppId());            // 应用ID
        String orderNo = "mho" + new Date().getTime();
        model.setMchOrderNo(orderNo);                       // 商户订单号
        model.setWayCode(wayCode);                          // 支付方式
        model.setAmount(1000l);                                // 金额，单位分
        model.setCurrency("USDT");                           // 币种，目前只支持cny
        model.setClientIp("192.166.6.153");                 // 发起支付请求客户端的IP地址
        model.setSubject("商品标题");                         // 商品标题
        model.setBody("商品描述");                            // 商品描述
        model.setNotifyUrl("https://www.jeequan.com");      // 异步通知地址
        model.setReturnUrl("");                             // 前端跳转地址
        model.setChannelExtra(channelExtra(wayCode));       // 渠道扩展参数
        model.setExtParam("");                              // 商户扩展参数,回调时原样返回

        request.setBizModel(model);
        try {
            PayOrderCreateResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));
            // 下单成功
            if(response.isSuccess(DePay.apiKey)) {
                String payOrderId = response.get().getPayOrderId();
                _log.info("payOrderId：{}", payOrderId);
                _log.info("mchOrderNo：{}", response.get().getMchOrderNo());
            }else {
                _log.info("下单失败：{}", orderNo);
                _log.info("通道错误码：{}", response.get().getErrCode());
                _log.info("通道错误信息：{}", response.get().getErrMsg());
            }
        } catch (DePayException e) {
            _log.error(e.getMessage());
        }

    }

    String channelExtra(String wayCode) {
        if("WX_JSAPI".equals(wayCode)) return wxJsapiExtra();
        if("WX_BAR".equals(wayCode)) return wxBarExtra();
        if("ALI_BAR".equals(wayCode)) return aliBarExtra();
        if("YSF_BAR".equals(wayCode)) return ysfBarExtra();
        if("UPACP_BAR".equals(wayCode)) return upacpBarExtra();
        if("QR_CASHIER".equals(wayCode)) return qrCashierExtra();
        if("AUTO_BAR".equals(wayCode)) return autoBarExtra();
        if("PP_PC".equals(wayCode)) return ppExtra();
        return "";
    }

    private String wxJsapiExtra() {
        JSONObject obj = new JSONObject();
        obj.put("openId", "134756231107811344");
        return obj.toString();
    }

    private String wxBarExtra() {
        JSONObject obj = new JSONObject();
        obj.put("authCode", "134675721924600802");
        return obj.toString();
    }

    private String aliBarExtra() {
        JSONObject obj = new JSONObject();
        obj.put("authCode", "1180812820366966512");
        return obj.toString();
    }

    private String ysfBarExtra() {
        JSONObject obj = new JSONObject();
        obj.put("authCode", "6223194037624963090");
        return obj.toString();
    }

    private String upacpBarExtra() {
        JSONObject obj = new JSONObject();
        obj.put("authCode", "6227662446181058584");
        return obj.toString();
    }

    private String qrCashierExtra() {
        JSONObject obj = new JSONObject();
        obj.put("payDataType", "codeImgUrl");
        return obj.toString();
    }

    private String autoBarExtra() {
        JSONObject obj = new JSONObject();
        obj.put("authCode", "134753177301492386");
        return obj.toString();
    }

    private String ppExtra() {
        JSONObject obj = new JSONObject();
        obj.put("cancelUrl", "http://baidu.com");
        return obj.toString();
    }

    @Test
    public void testPayOrderQuery() {
        // 支付接口文档：https://docs.jeequan.com/docs/DePay/payment_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        PayOrderQueryRequest request = new PayOrderQueryRequest();
        PayOrderQueryReqModel model = new PayOrderQueryReqModel();
        model.setMchNo(DePay.mchNo);                                           // 商户号
        model.setAppId(DePayClient.getAppId());                                // 应用ID
        model.setPayOrderId("P202106181104177050002");                          // 支付订单号
        request.setBizModel(model);

        try {
            PayOrderQueryResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));

            if(response.isSuccess(DePay.apiKey)) {
                _log.info("订单信息：{}", response);
                _log.info("金额：{}", response.get().getAmount());
            }
        } catch (DePayException e) {

            e.printStackTrace();
        }

    }

}
