package com.dechain.depay;

import com.dechain.depay.exception.DePayException;
import com.dechain.depay.model.DivisionReceiverBindReqModel;
import com.dechain.depay.request.DivisionReceiverBindRequest;
import com.dechain.depay.response.DivisionReceiverBindResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DivisionReceiverBindTest {

    final static Logger _log = LoggerFactory.getLogger(DivisionReceiverBindTest.class);

    @BeforeAll
    public static void initApiKey() {
        DePay.setApiBase(DepayTestData.getApiBase());
        DePay.apiKey = DepayTestData.getApiKey();
        DePay.mchNo = DepayTestData.getMchNo();
        DePay.appId = DepayTestData.getAppId();
    }

    @Test
    public void testDivisionReceiverBind() {
        // 分账接口文档：https://docs.jeequan.com/docs/DePay/division_api
        DePayClient DePayClient = DePayClient.getInstance(DePay.appId, DePay.apiKey, DePay.getApiBase());
        DivisionReceiverBindRequest request = new DivisionReceiverBindRequest();
        DivisionReceiverBindReqModel model = new DivisionReceiverBindReqModel();
        model.setMchNo(DePay.mchNo);                       // 商户号
        model.setAppId(DePayClient.getAppId());            // 应用ID
        model.setIfCode("shengpay");
        model.setReceiverAlias("计全");
        model.setReceiverGroupId(100003L);
        model.setAccType((byte)1);
        model.setAccNo("32617592");
        model.setAccName("骏易科技");
        model.setRelationType("SERVICE_PROVIDER");
        model.setRelationTypeName("服务商");
        model.setDivisionProfit("0.10");
        request.setBizModel(model);

        try {
            DivisionReceiverBindResponse response = DePayClient.execute(request);
            _log.info("验签结果：{}", response.checkSign(DePay.apiKey));
            // 判断转账发起是否成功（并不代表转账成功）
            if(response.isSuccess(DePay.apiKey)) {
                _log.info("accNo：{}， 绑定成功", response.get().getAccNo());
            }else {
                _log.info("绑定失败：accNo：{}", model.getAccNo());
                _log.info("通道错误码：{}", response.get().getErrCode());
                _log.info("通道错误信息：{}", response.get().getErrMsg());
            }
        } catch (DePayException e) {
            _log.error(e.getMessage());
        }

    }

}
