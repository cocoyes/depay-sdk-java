package com.dechain.depay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * DePay签名工具类
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public class DePayKit {

    private static String encodingCharset = "UTF-8";

    private static final Logger _log = LoggerFactory.getLogger(DePayKit.class);

    /**
     * 获取签名串
     * @param map
     * @return  urlParam.append(key).append("=").append( paraMap.get(key) == null ? "" : paraMap.get(key) );
     */
    public static String getStrSort(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(null != entry.getValue() && !"".equals(entry.getValue())){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
    }

    /**
     * <p><b>Description: </b>计算签名摘要
     * <p>2018年9月30日 上午11:32:46
     * @param map 参数Map
     * @param key 商户秘钥
     * @return
     */
    public static String getSign(Map<String,Object> map, String key){
        String result = getStrSort(map);
        result += "key=" + key;
        if(_log.isDebugEnabled()) _log.debug("signStr:{}", result);
        result = md5(result, encodingCharset).toUpperCase();
        if(_log.isDebugEnabled()) _log.debug("signValue:{}", result);
        return result;
    }

    public static String getSign(String signStr, String key){
        signStr += "key=" + key;
        String result = md5(signStr, encodingCharset).toUpperCase();
        return result;
    }

    public static String getSign(String signStr){
        return md5(signStr, encodingCharset).toUpperCase();
    }

    /**
     * <p><b>Description: </b>MD5
     * <p>2018年9月30日 上午11:33:19
     * @param value
     * @param charset
     * @return
     */
    public static String md5(String value, String charset) {
        MessageDigest md = null;
        try {
            byte[] data = value.getBytes(charset);
            md = MessageDigest.getInstance("MD5");
            byte[] digestData = md.digest(data);
            return toHex(digestData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }


}
