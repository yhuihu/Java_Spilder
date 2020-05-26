package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

public class Download implements Runnable {
    private String url;
    private String photoName;
    private String savePath;

    public Download(String name, String url, String savePath) {
        this.url = url;
        this.photoName = name;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        FileChannel outChannel = null;
        ReadableByteChannel readableByteChannel = null;
        //建立一个HTTP连接，使用输入流获得数据，使用输出流写入磁盘
        try {
            String file = savePath + photoName + ".jpg";
            URL url1 = new URL(url);
            readableByteChannel = Channels.newChannel(url1.openStream());
            outChannel = new FileOutputStream(file).getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            while (readableByteChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            //下载完成
            String name = Thread.currentThread().getName() + "-----------" + System.currentTimeMillis();
            System.out.println(name + "下载" + url);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //不论是否发生异常都会执行的
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (readableByteChannel != null) {
                try {
                    readableByteChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
