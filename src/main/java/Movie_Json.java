import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import util.Download;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Movie_Json {
    public static void main(String[] args) throws IOException {
        String savePath = "D:" + File.separator + "资源" + File.separator + "豆瓣电影排行榜" + File.separator;
        File file = new File(savePath);
        if (!file.exists()) {
            if (!file.mkdirs())
                System.out.println("系统操作出现异常。");
        }
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            String url = "https://movie.douban.com/j/search_subjects?type=movie&tag=%E8%B1%86%E7%93%A3%E9%AB%98%E5%88%86&sort=rank&page_limit=20&page_start=" + String.valueOf(i * 20);
            //尝试爬10页电影，按自己需求更改
            Connection.Response res = Jsoup.connect(url).ignoreContentType(true).execute();
            JSONObject json = JSONObject.parseObject(res.body());
            JSONArray jsonArray = json.getJSONArray("subjects");
            for (Object o : jsonArray) {
                JSONObject object = (JSONObject) o;
                //电影名称
                //System.out.println(object.getString("title"));
                //图片链接
                //System.out.println(object.getString("cover"));
                // 下载每张图片
                pool.execute(new Download(object.getString("title"), object.getString("cover"),savePath));
            }
        }
        pool.shutdown();
    }
}
