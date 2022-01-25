package com.dechain.depay;

import com.dechain.depay.exception.DePayException;
import com.dechain.depay.model.TransferOrderCreateReqModel;
import com.dechain.depay.model.TransferOrderQueryReqModel;
import com.dechain.depay.request.TransferOrderCreateRequest;
import com.dechain.depay.request.TransferOrderQueryRequest;
import com.dechain.depay.response.TransferOrderCreateResponse;
import com.dechain.depay.response.TransferOrderQueryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

class TransferOrderTest {

    final static Logger _log = LoggerFactory.getLogger(TransferOrderTest.class);

    @BeforeAll
    public static void initApiKey() {
        DePay.setApiBase(DepayTestData.getApiBase());
        DePay.apiKey = DepayTestData.getApiKey();
        DePay.mchNo = DepayTestData.getMchNo();
        DePay.appId = DepayTestData.getAppId();
    }

    @Test
    public void testTransferOrderCreate() {
        // 转账接口文档：https://docs.jeequan.com/docs/DePay/transfer_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        TransferOrderCreateRequest request = new TransferOrderCreateRequest();
        TransferOrderCreateReqModel model = new TransferOrderCreateReqModel();
        model.setMchNo(DePay.mchNo);                       // 商户号
        model.setAppId(DePayClient.getAppId());            // 应用ID
        model.setMchOrderNo("mho" + new Date().getTime());                // 商户转账单号
        model.setAmount(1L);
        model.setCurrency("CNY");
        model.setIfCode("wxpay");
        model.setEntryType("WX_CASH");
        model.setAccountNo("a6BcIwtTvIqv1zXZohc61biryWok");
        model.setAccountName("");
        model.setTransferDesc("测试转账");
        model.setClientIp("192.166.1.132");                 // 发起转账请求客户端的IP地址
        request.setBizModel(model);

        try {
            TransferOrderCreateResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));
            // 判断转账发起是否成功（并不代表转账成功）
            if(response.isSuccess(DePay.apiKey)) {
                String transferId = response.get().getTransferId();
                _log.info("transferId：{}", transferId);
                _log.info("mchOrderNo：{}", response.get().getMchOrderNo());
            }else {
                _log.info("下单失败：mchOrderNo={}, msg={}", model.getMchOrderNo(), response.getMsg());
                _log.info("通道错误码：{}", response.get().getErrCode());
                _log.info("通道错误信息：{}", response.get().getErrMsg());
            }
        } catch (DePayException e) {
            _log.error(e.getMessage());
        }

    }

    @Test
    public void testTransferOrderQuery() {
        // 转账接口文档：https://docs.jeequan.com/docs/DePay/transfer_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        TransferOrderQueryRequest request = new TransferOrderQueryRequest();
        TransferOrderQueryReqModel model = new TransferOrderQueryReqModel();
        model.setMchNo(DePay.mchNo);                                          // 商户号
        model.setAppId(DePayClient.getAppId());                               // 应用ID
        model.setTransferId("T202108121543441860003");                         // 转账单号
        request.setBizModel(model);
        try {
            TransferOrderQueryResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));
            if(response.isSuccess(DePay.apiKey)) {
                _log.info("订单信息：{}", response);
                _log.info("转账状态：{}", response.get().getState());
                _log.info("转账金额：{}", response.get().getAmount());
            }
        } catch (DePayException e) {
            e.printStackTrace();
        }

    }



}
