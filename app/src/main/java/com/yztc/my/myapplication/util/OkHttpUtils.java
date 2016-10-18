package com.yztc.my.myapplication.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by bodhixu on 2016/10/11.
 * OkHttp工具类
 */
public class OkHttpUtils {

    private static OkHttpUtils okHttpUtils;
    private static OkHttpClient client;

    /**
     * 返回工具类的单例
     * @return
     */
    private synchronized static OkHttpUtils newOkHttpUtilsInstance() {
        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils();
        }
        return okHttpUtils;
    }

    /**
     * 创建一个OkHttpClient的对象
     * @return
     */
    private synchronized static OkHttpClient newOkHttpClientInstance() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    //设置连接超时等属性
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS);

            client = builder.build();
        }
        return client;
    }

    //--------------------------GET 同步请求------------------------------
    /**
     * 获得Request实例
     * @param url
     * @return
     */
    private Request getRequest(String url) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        return builder.build();
    }

    /**
     * 获得Call实例
     * @param url
     * @return
     */
    private Call getCall(String url) {
        okHttpUtils = newOkHttpUtilsInstance();
        client = newOkHttpClientInstance();
        Request request = okHttpUtils.getRequest(url);
        return client.newCall(request);
    }

    /**
     * 获得Response对象
     * @param url
     * @return
     * @throws Exception
     */
    private Response getResponse(String url) throws Exception{
        Call call = getCall(url);
        return call.execute();
    }

    /**
     * 获得ResponseBody实例
     * @param url
     * @return
     * @throws Exception
     */
    private ResponseBody getResponseBody(String url) throws Exception{
        Response response = getResponse(url);
        if (response != null && response.isSuccessful()) {
            return response.body();
        }
        return null;
    }

    /**
     * 通过Url获得字符串 -- 同步的GET 请求
     * @param url
     * @return
     * @throws Exception
     */
    public static String getString(String url) throws Exception{
        okHttpUtils = newOkHttpUtilsInstance();
        ResponseBody responseBody = okHttpUtils.getResponseBody(url);
        if (responseBody != null) {
            return responseBody.string();
        }
        return null;
    }

    /**
     * 通过Url获得字节数组 -- 同步的GET 请求
     * @param url
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(String url) throws Exception{
        okHttpUtils = newOkHttpUtilsInstance();
        ResponseBody responseBody = okHttpUtils.getResponseBody(url);
        if (responseBody != null) {
            return responseBody.bytes();
        }
        return null;
    }

    /**
     * 通过Url获得输入流 -- 同步的GET 请求
     * @param url
     * @return
     * @throws Exception
     */
    public static InputStream getInputStream(String url) throws Exception{
        okHttpUtils = newOkHttpUtilsInstance();
        ResponseBody responseBody = okHttpUtils.getResponseBody(url);
        if (responseBody != null) {
            return responseBody.byteStream();
        }
        return null;
    }
    //--------------------------GET 同步请求------------------------------

    //--------------------------GET 异步请求------------------------------
    public static void doGETRequest(String url, Callback callback) {
        okHttpUtils = newOkHttpUtilsInstance();
        Call call = okHttpUtils.getCall(url);
        call.enqueue(callback);//执行异步的任务
    }
    //--------------------------GET 异步请求------------------------------

   //--------------------------POST 异步请求------------------------------
    /**
     * 获得Request实例
     * @param url
     * @return
     */
    private Request getRequest(String url, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(requestBody);
        return builder.build();
    }

    /**
     * 通过键值对创建RequestBody
     * @param map
     * @return
     */
    private RequestBody getRequestBody(HashMap<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        for (HashMap.Entry<String, String > entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return  builder.build();
    }

    /**
     * 获得Call实例
     * @param url
     * @return
     */
    private Call getCall(String url, HashMap<String, String> map) {
        okHttpUtils = newOkHttpUtilsInstance();
        client = newOkHttpClientInstance();
        RequestBody requestBody = okHttpUtils.getRequestBody(map);
        Request request = okHttpUtils.getRequest(url, requestBody);
        return client.newCall(request);
    }

    /**
     * 根据url和键值对，发送异步Post请求
     * @param url
     * @param map
     * @param callback
     */
    public static void doPostRequest(String url, HashMap<String, String> map, Callback callback) {
        okHttpUtils = newOkHttpUtilsInstance();
        Call call = okHttpUtils.getCall(url, map);
        call.enqueue(callback);
    }


    /**
     * 获取文件MimeType
     * @param filename
     * @return
     */
    private String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    /**
     * 根据请求参数的键值对和上传的文件生成RequestBody
     * @param map
     * @param fileNames
     * @return
     */
    private RequestBody getRequestBody(HashMap<String, String> map, List<String> fileNames) {
        MultipartBody.Builder builder = new MultipartBody.Builder(); //创建MultipartBody.Builder，用于添加请求的数据
        for (HashMap.Entry<String, String > entry : map.entrySet()) { //对键值对进行遍历
            builder.addFormDataPart(entry.getKey(), entry.getValue()); //把键值对添加到Builder中
        }
        for (int i=0; i<fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            String fileType = getMimeType(file.getName()); //根据文件的后缀名，获得文件类型
            builder.addFormDataPart( //给Builder添加上传的文件
                    "image",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
            );
        }
        return  builder.build(); //根据Builder创建请求
    }

    //发送请求： Call.enqueue(CallBack callback)
    //获得Call： client.newCall(Request request)
    //获得Request： Request.Build ------- > post(RequestBody)
    //获得RequestBody ：
    private Request getRequest(String url, HashMap<String, String> map, List<String> fileNames) {
        okHttpUtils = newOkHttpUtilsInstance();
        RequestBody requestBody = okHttpUtils.getRequestBody(map, fileNames);
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private Call getCall(String url, HashMap<String, String> map, List<String> fileNames) {
        client = newOkHttpClientInstance();
        okHttpUtils = newOkHttpUtilsInstance();
        Request request = okHttpUtils.getRequest(url, map, fileNames);
        return client.newCall(request);
    }

    public static void doPostRequest(String url, HashMap<String, String> map, List<String> fileNames, Callback callback) {
        okHttpUtils = newOkHttpUtilsInstance();
        Call call = okHttpUtils.getCall(url, map, fileNames);
        call.enqueue(callback);
    }

     //--------------------------POST 异步请求------------------------------

    //-----------------------------------解析Response----------------------------------------------------
    /**
     * 通过Response获得String
     * @param response
     * @return
     * @throws IOException
     */
    public static String getStringFromResponse(Response response) throws IOException{
        if (response != null && response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.string();
            }
        }
        return null;
    }

    /**
     * 根据响应获得字节数组
     * @param response
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromResponse(Response response) throws IOException{
        if (response != null && response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.bytes();
            }
        }
        return null;
    }

    /**
     * 根据响应获得输入流
     * @param response
     * @return
     * @throws IOException
     */
    public static InputStream getInputStreamFromResponse(Response response) throws IOException{
        if (response != null && response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.byteStream();
            }
        }
        return null;
    }

    /**
     * 取消所有为tag的Call
     * @param tag
     */
    public static void cancelCallsWithTag(Object tag) {

        if (tag == null) {
            return;
        }

        synchronized (client.dispatcher().getClass()) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }

            for (Call call : client.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }
        }
    }

}
