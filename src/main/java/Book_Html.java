import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Download;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Book_Html {
    public static void main(String[] args) {
        // 确定目标地址 URL 统一资源定位符
        String url = "https://book.douban.com/";
        String savePath = "D:" + File.separator + "资源" + File.separator + "豆瓣图书" + File.separator;
        File file = new File(savePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.out.println("系统操作出现异常。");
            }
        }
        // 2 解析 html
        try {
            //
            Document doc = Jsoup.connect(url).get();
            //从 Doc 的树形结构中查找 img 标签
            //.class 选择器
            Elements els = doc.select(".cover img");
            System.out.println(els.size());
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(9);
            for (Element e : els) {
                // <img src=""  width=""  height="" />
                String src = e.attr("src");
                System.out.println(src);
                // 下载每张图片
                pool.execute(new Download(UUID.randomUUID().toString().replace("-", ""), src, savePath));
            }
            //释放资源
            pool.shutdown();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
