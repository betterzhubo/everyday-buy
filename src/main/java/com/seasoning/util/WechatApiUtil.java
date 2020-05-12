package com.seasoning.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WechatApiUtil {

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.appSecret}")
    private String appSecret;

    @Value("${wechat.mchId}")
    private String mchId;

    @Value("${wechat.notifyUrl}")
    private String notifyUrl;

    private String ipAddr;

    private String CODE_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private String PLACE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    @PostConstruct
    public void init() throws UnknownHostException {
        this.ipAddr = InetAddress.getLocalHost().getHostAddress();//获得本机IP
    }

    public String code2Session(String code){

        Map<String, String> param = new HashMap<>();
        param.put("appid", appId);
        param.put("secret", appSecret);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        return HttpUtil.doGet(CODE_SESSION_URL, param);
    }

    public String placeOrder(Integer orderId, String totalFee){

        String result = HttpUtil.postXML(PLACE_ORDER_URL ,paramXml(orderId,totalFee));
        if(result == null){
            return null;
        }
        Map<String, String> map = CommonUtil.getXmlBodyContext(result);

        if(StringUtils.equals(map.get("return_code"), "FAIL")){
            log.error(result);
            return null;
        }
        if(StringUtils.equals(map.get("result_code"), "FAIL")){
            log.error(result);
            return null;
        }
        return map.get("prepay_id");
    }


    private String paramXml(Integer orderId, String totalFee){
        //生成的随机字符串
        //String nonce_str = StringUtils.getRandomStringByLength(32);
        long rand = RandomUtils.nextInt(100,999);
        String nonce_str = DateFormatUtils.format(new Date(), "yyyyMMdd")+rand;
        //订单日期
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
//        System.out.println(df.format(new Date()));
//        String orderDate = df.format(new Date());
        //商品名称
//        String body = wxMinPayIn.getOrderSubject();
        //获取本机的ip地址
//        String notify_url = "http://chengtianmai.cn:9999/order/notice";
        String signSource="appid="+appId+"&body=成天买调味品&mch_id="+mchId+"&nonce_str="+nonce_str+"&notify_url="+notifyUrl
                +"&out_trade_no="+orderId+"&spbill_create_ip="+this.ipAddr+"&total_fee="+totalFee+"&trade_type=JSAPI&key="+appSecret;
        System.out.println(signSource);
        String sign = MD5Util.EncryptionStr32(signSource).toUpperCase();
        //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败,（xml拼接字段依据自己公司需求而定）
        String xml = "<xml>"
                + "<appid>" + appId + "</appid>"
                + "<body>" + "成天买调味品" + "</body>"
                + "<mch_id>" + mchId + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + notifyUrl + "</notify_url>"
                + "<out_trade_no>" + orderId + "</out_trade_no>"
                + "<spbill_create_ip>" + this.ipAddr + "</spbill_create_ip>"
                + "<total_fee>" + totalFee + "</total_fee>"
                + "<trade_type>JSAPI</trade_type>"
                + "<sign>" + sign + "</sign>"
                + "</xml>";
        log.info("调试模式_统一下单接口 请求XML数据：" + xml);
        return xml;
    }
}
