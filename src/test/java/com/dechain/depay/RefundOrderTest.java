package com.dechain.depay;

import com.dechain.depay.exception.DePayException;
import com.dechain.depay.model.RefundOrderCreateReqModel;
import com.dechain.depay.model.RefundOrderQueryReqModel;
import com.dechain.depay.request.RefundOrderCreateRequest;
import com.dechain.depay.request.RefundOrderQueryRequest;
import com.dechain.depay.response.RefundOrderCreateResponse;
import com.dechain.depay.response.RefundOrderQueryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

class RefundOrderTest {

    final static Logger _log = LoggerFactory.getLogger(RefundOrderTest.class);

    @BeforeAll
    public static void initApiKey() {
        DePay.setApiBase(DepayTestData.getApiBase());
        DePay.apiKey = DepayTestData.getApiKey();
        DePay.mchNo = DepayTestData.getMchNo();
        DePay.appId = DepayTestData.getAppId();
    }

    @Test
    public void testRefundOrderCreate() {
        // 接口文档：https://docs.jeequan.com/docs/DePay/refund_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        RefundOrderCreateRequest request = new RefundOrderCreateRequest();
        RefundOrderCreateReqModel model = new RefundOrderCreateReqModel();
        model.setMchNo(DePay.mchNo);                       // 商户号
        model.setAppId(DePayClient.getAppId());            // 应用ID
        model.setMchOrderNo("");                            // 商户支付单号(与支付订单号二者传一)
        model.setPayOrderId("P202106181104177050002");      // 支付订单号(与商户支付单号二者传一)
        String refundOrderNo = "mho" + new Date().getTime();
        model.setMchRefundNo(refundOrderNo);                // 商户退款单号
        model.setRefundAmount(4l);                          // 退款金额，单位分
        model.setCurrency("cny");                           // 币种，目前只支持cny
        model.setClientIp("192.166.1.132");                 // 发起支付请求客户端的IP地址
        model.setRefundReason("退款测试");                    // 退款原因
        model.setNotifyUrl("https://www.jeequan.com");      // 异步通知地址
        model.setChannelExtra("");                          // 渠道扩展参数
        model.setExtParam("");                              // 商户扩展参数,回调时原样返回
        request.setBizModel(model);
        try {
            RefundOrderCreateResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));
            // 判断退款发起是否成功（并不代表退款成功）
            if(response.isSuccess(DePay.apiKey)) {
                String refundOrderId = response.get().getRefundOrderId();
                _log.info("refundOrderId：{}", refundOrderId);
                _log.info("mchRefundNo：{}", response.get().getMchRefundNo());
            }else {
                _log.info("下单失败：refundOrderNo={}, msg={}", refundOrderNo, response.getMsg());
                _log.info("通道错误码：{}", response.get().getErrCode());
                _log.info("通道错误信息：{}", response.get().getErrMsg());
            }
        } catch (DePayException e) {
            _log.error(e.getMessage());
        }

    }

    @Test
    public void testRefundOrderQuery() {
        // 接口文档：https://docs.jeequan.com/docs/DePay/refund_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        RefundOrderQueryRequest request = new RefundOrderQueryRequest();
        RefundOrderQueryReqModel model = new RefundOrderQueryReqModel();
        model.setMchNo(DePay.mchNo);                                             // 商户号
        model.setAppId(DePayClient.getAppId());                                  // 应用ID
        model.setRefundOrderId("P202106181105527690009");                         // 退款单号
        request.setBizModel(model);
        try {
            RefundOrderQueryResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));
            if(response.isSuccess(DePay.apiKey)) {
                _log.info("订单信息：{}", response);
                _log.info("退款状态：{}", response.get().getState());
                _log.info("退款金额：{}", response.get().getRefundAmount());
            }
        } catch (DePayException e) {
            e.printStackTrace();
        }

    }



}
