package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Download implements Runnable {
    private String url;
    private String photoName;
    private String savePath;

    public Download(String name, String url,String savePath) {
        this.url = url;
        this.photoName = name;
        this.savePath=savePath;
    }

    @Override
    public void run() {
        //建立一个HTTP连接，使用输入流获得数据，使用输出流写入磁盘
        HttpURLConnection conn = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(3*1000);
            conn.setReadTimeout(3*1000);
            //读取数据
            in = conn.getInputStream();
            String file = savePath + photoName + ".jpg";
            //创建输出流，写入
            out = new FileOutputStream(file);

            byte[] buf = new byte[1024 + 16];
            int size;
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            //下载完成
            String name = Thread.currentThread().getName() + "-----------" + System.currentTimeMillis();
            System.out.println(name + "下载" + url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //不论是否发生异常都会执行的
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}