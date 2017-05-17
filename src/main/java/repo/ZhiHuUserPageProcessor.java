package repo;

/**
 * Created by zoujing on 2017/5/17.
 */
import dao.ZhihuDao;
import dao.impl.ZhihuDaoImpl;
import entity.ZhihuUser;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
import java.util.List;

/**
 * 知乎用户小爬虫<br>
 * 输入搜索用户关键词(keyword)，并把搜出来的用户信息爬出来<br>

 * @date 2016-5-3
 * @website ghb.soecode.com
 * @csdn blog.csdn.net/antgan
 * @author antgan
 *
 */
public class ZhiHuUserPageProcessor implements PageProcessor{
    //抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);
    //用户数量
    private static int num = 0;
    //搜索关键词
    private static String keyword = "程序员";
    //数据库持久化对象，用于将用户信息存入数据库
    private ZhihuDao zhihuDao = new ZhihuDaoImpl();


    /**
     * process 方法是webmagic爬虫的核心
     * 编写抽取【待爬取目标链接】的逻辑代码在html中。
     */

    public void process(Page page) {

        //1. 如果是用户列表页面 【入口页面】，将所有用户的详细页面的url放入target集合中。
        if(page.getUrl().regex("https://www\\.zhihu\\.com/search\\?type=people&q=[\\s\\S]+").match()){
            List<String> requests=page.getHtml().xpath("//ul[@class='list users']/li/div/div[@class='body']/div[@class='line']").links().all();
            for(int i=0;i<requests.size();i++){
                requests.set(i,requests.get(i)+"/answers");
            }


            System.out.print(requests);


            page.addTargetRequests(requests);
        }
        //2. 如果是用户详细页面
        else{
            num++;//用户数++

            /*实例化ZhihuUser，方便持久化存储。*/
            ZhihuUser user = new ZhihuUser();
            /*从下载到的用户详细页面中抽取想要的信息，这里使用xpath居多*/
            /*为了方便理解，抽取到的信息先用变量存储，下面再赋值给对象*/
            String name = page.getHtml().xpath("//h1[@class='ProfileHeader-title']/span[@class='ProfileHeader-name']/text()").get();

            String identity = page.getHtml().xpath("h1[@class='ProfileHeader-title']/span[@class='RichTest ProfileHeader-headline']/text()").get();

            String profession = page.getHtml().xpath("//div[@class='ProfileHeader-infoItem']/div[@class='ProfileHeader-iconWrapper']/text()").get();

            String gender=page.getHtml().xpath("//div[@class='ProfileHeader-infoItem']/div[@class='ProfileHeader-iconWrapper']/svg/@class").get();

            int sex = -1;

            //sex取值
            if(gender=="Icon Icon--male"){
                sex=1;
            }else if(gender=="Icon Icon--company"){
                sex=2;
            }else if(gender=="Icon Icon--female"){
                sex=0;
            }else{
                sex=4;
            }


//
//            String school = " ";
//            String major = " ";
//            String recommend = " ";
//
//            String location =" ";
//
//

            String picUrl = page.getHtml().xpath("//div[@class='UserAvatar ProfileHeader-avatar']/img[@class='Avatar Avatar--large UserAvatar-inner']/@src").get();


//            System.out.println(num+","+name+"，"+identity+","+gender+","+sex+","+profession+","+picUrl);

//
//
//            int agree = Integer.parseInt(page.getHtml().xpath("//span[@class='zm-profile-header-user-agree']/strong/text()").get());
//            int thanks = Integer.parseInt(page.getHtml().xpath("//span[@class='zm-profile-header-user-thanks']/strong/text()").get());
//            int ask = Integer.parseInt(page.getHtml().xpath("//div[@class='profile-navbar clearfix']/a[2]/span[@class='num']/text()").get());
//            int answer = Integer.parseInt(page.getHtml().xpath("//div[@class='profile-navbar clearfix']/a[3]/span[@class='num']/text()").get());
//            int article = Integer.parseInt(page.getHtml().xpath("//div[@class='profile-navbar clearfix']/a[4]/span[@class='num']/text()").get());
//            int collection = Integer.parseInt(page.getHtml().xpath("//div[@class='profile-navbar clearfix']/a[5]/span[@class='num']/text()").get());

            //对象赋值
            user.setKey(keyword);
            user.setName(name);
            user.setIdentity(identity);
            user.setLocation("");
            user.setProfession(profession);
            user.setSex(sex);
            user.setSchool(" ");
            user.setMajor(" ");
            user.setRecommend(" ");
            user.setPicUrl(picUrl);
            user.setAgree(0);
            user.setThanks(0);
            user.setAsk(0);
            user.setAnswer(0);
            user.setArticle(0);
            user.setCollection(0);


            System.out.println("执行saveuser");

            System.out.println("num:"+num +" " + user.toString());//输出对象
            zhihuDao.saveUser(user);//保存用户信息到数据库
        }
    }





    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        long startTime ,endTime;
        System.out.println("========知乎用户信息小爬虫【启动】喽！=========");
        startTime = new Date().getTime();
        //入口为：【https://www.zhihu.com/search?type=people&q=xxx 】，其中xxx 是搜索关键词
        Spider spider= Spider.create(new ZhiHuUserPageProcessor()).addUrl("https://www.zhihu.com/search?type=people&q="+keyword);
        spider.thread(5);
        spider.run();
        endTime = new Date().getTime();
        System.out.println("========知乎用户信息小爬虫【结束】喽！=========");
        System.out.println("一共爬到"+num+"个用户信息！用时为："+(endTime-startTime)/1000+"s");
    }

}