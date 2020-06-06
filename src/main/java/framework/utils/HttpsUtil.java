package framework.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * @program: wady
 * @description: TODO
 * @author: YJiang（叶闲）
 * @create: 2020-04-26 20:33
 */
public class HttpsUtil {

    /**
     * 绕过SSL验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] {trustManager}, null);
        return sslContext;
    }

    /**
     * 绕过SSL证书，发送Get请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static String doIgnoreVerifySSLGet(String url, Map<String, Object> params)
        throws IOException, KeyManagementException, NoSuchAlgorithmException {
        //采用绕过验证的方式处理https请求
        SSLContext sslContext = createIgnoreVerifySSL();
        final SSLConnectionSocketFactory sslsf;

        //设置协议http和https对应的处理socket链接工厂的对象
        sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new PlainConnectionSocketFactory())
            .register("https", sslsf)
            .build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);

        //创建自定义的httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .setConnectionManager(cm)
            .build();

        String result = null;
        //装填参数
        StringBuffer param = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
                i++;
            }
            url += param;
        }
        // 创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {

            // 获取结果实体
            HttpEntity httpEntity = response.getEntity();

            // 按指定编码转换结果实体为String类型
            result = EntityUtils.toString(httpEntity, "UTF-8");
        }
        //释放链接
        response.close();
        return result;
    }

    /**
     * 绕过SSL证书，发送Post请求（Json形式）
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static String doIgnoreVerifySSLPost(String url, JSONObject param)
        throws IOException, KeyManagementException, NoSuchAlgorithmException {

        //采用绕过验证的方式处理https请求
        SSLContext sslContext = createIgnoreVerifySSL();
        final SSLConnectionSocketFactory sslsf;

        //设置协议http和https对应的处理socket链接工厂的对象
        sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new PlainConnectionSocketFactory())
            .register("https", sslsf)
            .build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);

        //创建自定义的httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .setConnectionManager(cm)
            .build();

        String result = null;

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        //设置参数到请求对象中
        httpPost.setEntity(entity);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, "
            + "like Gecko) Chrome/80.0.3987.163 Safari/537.36");

        httpPost.setHeader("Cookie",
            "bs_n_lang=zh_CN; lvc=sAhk%2F4grVpGcmQ%3D%3D; emplId=245087; "
                + "UM_distinctid=171b66e25cdb7d-08b5db1674ec7a-396e7507-13c680-171b66e25ce440; "
                + "hsfops_USER_COOKIE"
                +
                "=D54ACB2BED59171D01F7AA48D002217C614E526665E76257DAB55C67A20D143122ADA180A4F736F50E79CB43272B1B5BF7A83DC18F76B371B9F64EC0C497A44D5BAD75562E3E52E3DD14B6781FCC83D366E99F8CA5D76FCB94BF21E292D1BD84; hsfops_SSO_TOKEN_V2=44BAB6525ABED34D8EC51063626DA64B3F6FA82994A23643E9360177D1F47C727A5EB06B4BC8ADFB74B9857E36FABF4704AA27208E115A43F8B6DE99C90DAAD2; l=Altbbyv-vuV2JqOWPcj5L-nUa7TFMG8y; isg=A1C5F41FECE5C4EE3070A02A1D5039E7");

        httpPost.setHeader("Content-Type", "application/json");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            //获取结果实体
            HttpEntity httpEntity = response.getEntity();
            //按指定编码转换结果实体为String类型
            result = EntityUtils.toString(httpEntity, "UTF-8");
        }

        //释放链接
        response.close();

        return result;
    }

}
