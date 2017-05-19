package repo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.ZhihuPageProcessor;

/**
 * Created by zoujing on 2017/5/18.
 */
public class test2PageProcessor implements PageProcessor{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);


    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("https://www\\.zhihu\\.com/question/\\d+/answer/\\d+.*").all());
        page.putField("title", page.getHtml().xpath("//h2[@class='zm-item-title']/a/text()").toString());
        page.putField("question", page.getHtml().xpath("//div[@id='zh-question-detail']//tidyText()").toString());
        page.putField("answer", page.getHtml().xpath("//div[@id='zh-question-answer-wrap']//div[@class='zm-editable-content']/tidyText()").toString());
        if (page.getResultItems().get("title")==null){
            //skip this page
            page.setSkip(true);
        }
    }


    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ZhihuPageProcessor()).addUrl("https://www.zhihu.com/explore")

                .run();
    }
}
