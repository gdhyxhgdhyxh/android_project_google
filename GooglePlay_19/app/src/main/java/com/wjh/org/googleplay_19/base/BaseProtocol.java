package com.wjh.org.googleplay_19.base;

import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjh.org.googleplay_19.config.Constants;
import com.wjh.org.googleplay_19.utils.FileUtils;
import com.wjh.org.googleplay_19.utils.HttpUtils;
import com.wjh.org.googleplay_19.utils.IOUtils;
import com.wjh.org.googleplay_19.utils.LogUtils;
import com.wjh.org.googleplay_19.utils.UIUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * @创建者     wjh
 * @创建时间   2016/2/28 10:47
 * @描述	      ${TODO}
 *
 * @版本       $Revision$
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */

/**
 * 网络协议的封装为基类
 *
 * @param <T> 具体类是不知道的,所以定义为泛型
 */
public abstract class BaseProtocol<T> {

    private Map<String, String> mCacheMap;//存储结构的集合

    /**
     * 加载数据
     *
     * @param index 分页的索引
     * @return 网络请求回来对应的类
     * @throws IOException
     */
    public T loadData(int index) throws IOException {

        //初始化数据解析的泛型对象
        T t = null;

        //获取缓存数据的唯一标记
        String key = generateKey(index);
        // 获取存储结构的集合,保存成成员变量,全局调用
        MyApplication app = (MyApplication) UIUtils.getContext();
        mCacheMap = app.getCacheMap();

        //1. 先找内存,在内存有-->返回缓存
        t = loadDtaFromMem(key);
        if (t != null) {
            return t;
        }

        //2.在本地-->存内存,返回
        t = loadDataFromDisk(key);
        if (t != null) {
            return t;
        }

        //3.在网络-->存内存,存本地,返回
        return loadDataFromNet(index);
    }

    /**
     * 1. 先找内存,在内存有-->返回缓存
     *
     * @param key 缓存数据唯一的凭证
     * @return 内存缓存
     */
    private T loadDtaFromMem(String key) {

        //1.1 先内存-->返回
        if (mCacheMap.containsKey(key)) {
            //1.2 如果包含当前的key,就代表内存有,获取内存的缓存
            String menCacheJsonString = mCacheMap.get(key);
            LogUtils.d("Test", "内存有缓存");
            //1.3 返回获取的缓存数据
            return processJsonString(menCacheJsonString);
        }
        return null;
    }

    /**
     * 2. 在本地磁盘有-->就存内存,返回
     *
     * @param key 缓存数据唯一的凭证
     * @return 磁盘的缓存
     */
    private T loadDataFromDisk(String key) {

        BufferedReader reader = null;
        try {

            File CacheFile = getCacheFile(key);
            if (CacheFile.exists()) {
                //1.1 如果当前文件夹存在,说明磁盘里有缓存
                reader = new BufferedReader(new FileReader(CacheFile));
                //1.2 读取第一行的缓存插入时间,并转换成long类型
                String firstLine = reader.readLine();
                long insertTime = Long.parseLong(firstLine);

                if (System.currentTimeMillis() - insertTime < Constants.PROTOCOLTIMEOUT) {
                    //2. 缓存时间没有过期,读取第二行的缓存
                    String diskCacheJsonString = reader.readLine();

                    //2.1 保存缓存数据到集合
                    mCacheMap.put(key, diskCacheJsonString);
                    LogUtils.d("Test", "本地磁盘有缓存");
                    //2.2 返回磁盘的数据给json解析者
                    return processJsonString(diskCacheJsonString);
                }
                //时间过期了,返回空
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源,关闭流对象
            IOUtils.close(reader);
        }
        return null;
    }

    /**
     * 获取本地磁盘缓存文件
     *
     * @param key 缓存数据唯一的标记
     * @return
     */
    @NonNull
    private File getCacheFile(String key) {
        String dir = FileUtils.getDir("json"); //文件路径
        String fileName = key;                  //文件名
        return new File(dir, fileName);
    }

    /**
     * 根据分页索引,创建一个唯一索引的key
     *
     * @param index 分页索引
     */
    private String generateKey(int index) {

        String key = null;

        //遍历保存了唯一凭证的缓存标记
        Map<String, Object> paramsMap = getRequestParamsMap(index);
        for (Map.Entry<String, Object> params : paramsMap.entrySet()) {

            //获取Value,不关心key(点击什么,就获取什么)
            Object value = params.getValue();

            //1. 保证缓存数据唯一索引(协议关键字+分页索引)
            key = getInterfaceKey() + "." + value;
        }
        return key;
    }

    /**
     * 3. 从网络加载数据
     *
     * @param index 分页的索引
     * @return 网络请求回来对应类的数据
     * @throws IOException
     */
    private T loadDataFromNet(int index) throws IOException {
        //使用OkHttp框架来请求网络数据

        //1.构建OkHttpclient实例对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //2.1 url 地址
        String url = Constants.URL.BASE_URL + getInterfaceKey();
        //http://localhost:8080/GooglePlayServer/home?index=0

        //2.2 这里抽取成一个方法获得协议关键字后面的url参数
        Map<String, Object> params = getRequestParamsMap(index);

        //2.3 取出集合的参数,然后与url拼接
        String urlParamsByMap = HttpUtils.getUrlParamsByMap(params);

        //拼接url参数,要自己把?带上,?index=0
        url = url + "?" + urlParamsByMap;

        //2.构建请求对象
        Request request = new Request.Builder().get().url(url).build();

        //3.发送请求 --->同步
        Response response = okHttpClient.newCall(request).execute();

        boolean successful = response.isSuccessful();
        if (successful) { //请求成功了

            //4.取出请求的结果
            String resultJsonString = response.body().string();

            //4.1 保存缓存数据到内存
            mCacheMap.put(generateKey(index), resultJsonString);
            LogUtils.d("Test", "保存网络数据到内存");

            //4.2 保存缓存数据到磁盘文件
            writeData2Disk(index, resultJsonString);
            LogUtils.d("Test", "保存网络数据到本地磁盘内存");

            //5. 由于这里解析返回的数据不确定,有可能是Bean,list,所以要用到泛型解析的方式传递给调用者
            return processJsonString(resultJsonString);
        }

        return null;
    }

    /**
     * 1.把url参数添加到集合,使用工具类拼接参数
     * 2.由于网络协议后面的关键字不一样,所以定义成一个方法,子类可以复写;
     * 3.当子类复写的时候,优先调用子类的;
     * 4.默认情况是  index ,其它情况是子类复写之后
     * @param index
     * @return
     */
    public Map<String, Object> getRequestParamsMap(int index) {
        Map<String, Object> params = new HashMap<>();
        params.put("index", index);
        return params;
    }

    /**
     * 保存网络数据到磁盘本地文件
     *
     * @param index 分页索引
     * @param resultJsonString 网络数据
     */
    private void writeData2Disk(int index, String resultJsonString) {

        BufferedWriter bw = null;
        try {
            //1. 获取缓存文件位置
            File cacheFile = getCacheFile(generateKey(index));
            bw = new BufferedWriter(new FileWriter(cacheFile));

            //1.1 在第一行写入当前时间戳
            bw.write(System.currentTimeMillis()+"");
            //1.2 换行
            bw.newLine();
            //1.3 在第二行写入具体缓存数据
            bw.write(resultJsonString);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            IOUtils.close(bw);
        }

    }


    /**
     * 1.获取网络协议的关键字;
     * 2.定义为抽象方法,基类不知道具体实现是什么,所以让子类必须重写
     *
     * @return 网络协议的关键字;
     */
    public abstract String getInterfaceKey();

    /**
     * 1.返回解析的json数据;
     * 2.定义为抽象泛型方法,基类不知道具体解析的数据,所以让子类必须重写,自己实现具体内容
     *
     * @param resultJsonString
     * @return 解析后对应的bean, list或者其它
     */
    public abstract T processJsonString(String resultJsonString);
}
