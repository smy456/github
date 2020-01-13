package fh;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;


import java.io.IOException;

public class Test7 {
   @Test
   public void TestGet(){
        //
       CloseableHttpClient httpClient = HttpClientBuilder.create().build();
       HttpGet httpGet = new HttpGet("http://www.baidu.com");
       CloseableHttpResponse execute =null;
       try {
           execute = httpClient.execute(httpGet);
           HttpEntity entity = execute.getEntity();
           String string = EntityUtils.toString(entity, "utf-8");
           System.out.println(string);

       } catch (IOException e) {
           e.printStackTrace();
       }finally{
           if(execute!=null) {
               try {
                   execute.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
          }
           if(httpClient!=null) {
           try {
               httpClient.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

   }
   }


