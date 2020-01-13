package fh;

import javax.swing.text.html.parser.Entity;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class TestOCRClient {


    //腾讯云ocr身份证文字识别
    //腾讯云文字识别服务器域名
    public static final String HOST = "recognition.image.myqcloud.com";
    //使用 application/json 格式，参数为 url ，其值为图片链接
    public static final long APPID=1300008613;
    //secretida
    public static final String SECRET_ID = "AKIDiNrAJHW74pSt8RoTGxUrBKxOelCTfh2L";
    //secretkey
    public static final String SECRET_KEY= "ibez9aJKJ7kxyoFdqumV1QlwcA3mfMTU";
    //请求的域名
    public static final String TARGETURL = "https://recognition.image.myqcloud.com/ocr/idcard";
    //图片路径
    public static final String IDURL="https://lxpupload.oss-cn-beijing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20191007171032.jpg?Expires=1573474718&OSSAccessKeyId=TMP.hgefjfwZqb5JCrotaNwZEoEoVWpvEzAWFBT85HoAGpXFAt3wGYGvDDw28wLT98Prpt83pdu2F62txgK7sG6f8Ws1jJebthjcXiX7cBxtDGrFngSwRFDW5yXincvRAB.tmp&Signature=RtruxWSPoT7udtEU9z2KnIsjIgM%3D";
    public static void main(String[] args) {
        try {
            //发送http请求，首先要创建一个httpclient；
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //创建一个post请求
            HttpPost httpPost = new HttpPost(TARGETURL);
            //组装请求头信息
            httpPost.setHeader("host", "recognition.image.myqcloud.com");
            httpPost.setHeader("content-type", "application/json");
            httpPost.setHeader("authorization", Sign.appSign(APPID, SECRET_ID, SECRET_KEY, null, 0));
            //组装接口需要的参数
            JSONObject json = new JSONObject();
            json.put("appid", APPID);
            json.put("card_type", 0);
            json.put("url_list", IDURL);
            //将需要的参数放入http请求中
            StringEntity stringEntity = new StringEntity(json.toString(), "utf-8");
            httpPost.setEntity(stringEntity);
            //发送请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            System.out.println(code);
            System.out.println(string);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

}

