package com.love.lixinxin.httputils.okhttp;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataApiForOkHttp {

    public OkHttpClient http;

    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DataApiForOkHttp() {

        http = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void get(String url, EventRequestCallback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method(GET_METHOD, null);
        Request request = requestBuilder.build();
        Call mcall = http.newCall(request);
        callback.setmCall(mcall);
        mcall.enqueue(new EventRequestCallbackImpl(callback));
    }

    public void post(FormBody body, String url, EventRequestCallback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        //Builder builder = new Builder();
        //FormBody body = builder.add("", "").build();
        requestBuilder.post(body).build();
        Request request = requestBuilder.post(body).build();
        Call mcall = http.newCall(request);
        mcall.enqueue(new EventRequestCallbackImpl(callback));
    }


    public void getData(String url, EventRequestCallback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method(GET_METHOD, null);
        Request request = requestBuilder.build();
        Call mcall = http.newCall(request);
        callback.setmCall(mcall);
        mcall.enqueue(new MyEventRequestCallbackImpl(callback));
    }


    public class EventRequestCallbackImpl implements Callback {

        private EventRequestCallback mCallback;

        private EventResponseEntity resEntity = new EventResponseEntity();

        public EventRequestCallbackImpl(EventRequestCallback callback) {
            this.mCallback = callback;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback._onStart();
                }
            });
        }

        @Override
        public void onFailure(Call arg0, IOException arg1) {
            resEntity.code = Config.HTTP_REQUEST_FAILURE;
            resEntity.message = "网络异常";
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback._RequestCallback(resEntity);
                }
            });
        }

        @Override
        public void onResponse(Call arg0, Response response) throws IOException {
            if (response.isSuccessful()) {
                String res = response.body().string();
                try {
                    resEntity = GsonUtils.asJSONToResponseEntity(res);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback._RequestCallback(resEntity);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    resEntity.code = Config.HTTP_REQUEST_JSON_ERROR;
                    resEntity.message = "数据格式错误";
                    resEntity.data = res;
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            mCallback._RequestCallback(resEntity);
                        }
                    });
                }
            }
        }
    }

    /**
     * 返回数据不解析
     */
    public class MyEventRequestCallbackImpl implements Callback {

        private EventRequestCallback mCallback;

        private EventResponseEntity resEntity = new EventResponseEntity();

        public MyEventRequestCallbackImpl(EventRequestCallback callback) {
            this.mCallback = callback;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback._onStart();
                }
            });
        }


        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String res = response.body().string();
            resEntity.code = Config.HTTP_REQUEST_SUCCESS;
            resEntity.data = res;
            resEntity.message = "执行成功";

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback._RequestCallback(resEntity);
                }
            });
        }

        @Override
        public void onFailure(Call call, IOException e) {
            resEntity.code = Config.HTTP_REQUEST_FAILURE;
            resEntity.data = "";
            resEntity.message = "网络异常";
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback._RequestCallback(resEntity);
                }
            });
        }
    }

}
