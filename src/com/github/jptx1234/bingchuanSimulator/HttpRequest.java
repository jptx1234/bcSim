package com.github.jptx1234.bingchuanSimulator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url 发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        String line;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "www/source, text/html, video/mpeg, image/jpeg, image/x-tiff");
            connection.setRequestProperty("accept", "image/x-rgb, image/x-xbm, image/gif, */*, application/postscript");
            connection.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            connection.setConnectTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            while ((line = in.readLine()) != null) {
                result += line;
            }
            connection.disconnect();
        } catch (IOException e) {
        	line = "与认证服务器连接出错:\r\n"+e.toString();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}