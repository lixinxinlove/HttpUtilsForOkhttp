package com.love.lixinxin.httputils.okhttp;

import java.util.Iterator;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.FormBody.Builder;

public class DataApi {

    public DataApiForOkHttp okHttp;

    public DataApi() {
        okHttp = new DataApiForOkHttp();
    }

    /**
     * 百度
     *
     * @param requestCallback
     */
    public void baidu(EventRequestCallback requestCallback) {
        String url = "http://www.baidu.com";
        okHttp.getData(url, requestCallback);
    }

    /**
     * 登录
     *
     * @param chimelongCallback
     * @param username
     * @param password
     */
    public void login(EventRequestCallback chimelongCallback, String username, String password) {

        String url = "";


        Builder builder = new Builder();
        FormBody body = builder
                .add("username", username)
                .add("password", password)
                .add("app_id", Config.REQUEST_APPID_VALUE)
                .build();
        okHttp.post(body, url, chimelongCallback);
    }


    /**
     * 微信登陆2
     *
     * @param requestCallback
     * @param url
     */
    public void wxLogin(EventRequestCallback requestCallback, String url) {
        okHttp.getData(url, requestCallback);
    }


    public void getTickets(EventRequestCallback eventCallback, String org_id, String ticket_id, String lastReqDate, String page, String size) {
        //TreeMap<String, String> tree = createBaseParams();
        String url = "";

        Builder builder = new Builder();
        FormBody body = builder
                .add("ticket_id", ticket_id)
                .add("app_id", Config.REQUEST_APPID_VALUE)
                .add("page", page)
                .add("size", size)
                .build();
        okHttp.post(body, url, eventCallback);
    }


    /**
     * 获取加密的sign
     *
     * @param tree
     * @return
     */

    private String getRequestTokenMD5(TreeMap<String, String> tree) {
        StringBuffer secretBuffer = new StringBuffer();
        Iterator<String> it = tree.keySet().iterator();
        secretBuffer.append(Config.MD5_SECRET);
        while (it.hasNext()) {
            String key = it.next();
            String value = tree.get(key);
            secretBuffer.append(key + value);
        }
        secretBuffer.append(Config.MD5_SECRET);
        String sign = Config.MD5_SECRET;
        return sign;
    }
}
