package repo;

/**
 * Created by zoujing on 2017/5/17.
 */
import dao.ZhihuDao;
import dao.impl.ZhihuDaoImpl;
import entity.ZhihuUser;

import proxy.HttpUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.proxy.SimpleProxyPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 知乎用户小爬虫
 * 输入搜索用户关键词(keyword)，并把搜出来的用户信息爬出来

 * @date 2017-5-17
 * @website wustzoujing.cn
 * @author zoujing
 *
 */
public class ZhiHuUserPageProcessor implements PageProcessor{
    //抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(10).setSleepTime(1000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
            .setDomain("www.zhihu.com");


//
    public List<String[]> ipList = new ArrayList();
    public BufferedReader proxyIpReader = new BufferedReader(new InputStreamReader(HttpUtils.class.getResourceAsStream("/proxyip.txt")));



    //用户数量
    private static AtomicInteger num = new AtomicInteger(0);
    //搜索关键词，这里用我的id
    private static String keyword = "陈萌萌";
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


//            System.out.print(requests);

            page.addTargetRequests(requests);
        }
        //2. 如果是用户详细页面
        else if(page.getUrl().regex("https://www\\.zhihu\\.com/people/(.+)/answers").match())
        {
            num.getAndIncrement();//用户数++

//            System.out.println(page.getUrl().get());

            String html=page.getHtml().get();

//            System.out.println(html);

            Element userUrlContent = null;

            userUrlContent=Jsoup.parse(html);



            String userContent = userUrlContent.text();

//            System.out.print(userContent);

            /*实例化ZhihuUser，方便持久化存储。*/
            ZhihuUser user = new ZhihuUser();

            String name = page.getHtml().xpath("//h1[@class='ProfileHeader-title']/span[@class='ProfileHeader-name']/text()").get();

//            System.out.println(name+" ");

//            String identity = page.getHtml().xpath("h1[@class='ProfileHeader-title']/span[@class='RichTest ProfileHeader-headline']/text()").get();

            String identity = page.getHtml().xpath("//*[@id='ProfileHeader']/div/div[2]/div/div[2]/div[1]/h1/span[2]/text()").get();

//            System.out.println(identity+" ");
            //对象赋值

            user.setName(name);

            user.setIdentity(identity);


            int infoItem_count = page.getHtml().xpath("//div[@class='ProfileHeader-infoItem']").nodes().size();

            if(infoItem_count==2){

                String string1 = userUrlContent.select(".ProfileHeader-infoItem").first().text();


                if (string1 != null && string1 != "") {
                    String[] a = string1.split(" ");
                    //行业
                    for (int i = 0; i < a.length; i++) {
                        if (a.length > 0) {
                            user.setProfession(a[0]);;
                        }
                        //公司
                        if (a.length > 1) {
                            user.setCompany(a[1]);
                        }
                        //职位
                        if (a.length > 2) {
                            user.setPosition(a[2]);
                        }
                    }
                }
                String string2 = userUrlContent.select(".ProfileHeader-infoItem").get(1).text();
                if (string2 != null && string2 != "") {
                    String[] a = string2.split(" ");
                    //学校
                    if (a.length > 0) {
                        user.setSchool(a[0]);
                    }
                    //专业
                    if (a.length > 1) {
                        user.setMajor(a[1]);
                    }
                }

            }

//            System.out.println(user.getCompany()+" "+user.getSchool()+" "+user.getMajor()+" "+user.getPosition()+" "+user.getProfession());



            //看‘关注他’中有无关键字，判断性别
            String sexString = page.getHtml().xpath("//button[@class='Button FollowButton Button--primary Button--blue']/text()").get();

            if (sexString.contains("他")) {
                user.setSex(0);
            } else if (sexString.contains("她")) {
                user.setSex(1);
            } else {
                user.setSex(2);
            }


            String picUrl = page.getHtml().xpath("//div[@class='UserAvatar ProfileHeader-avatar']/img[@class='Avatar Avatar--large UserAvatar-inner']/@src").get();
            user.setPicUrl(picUrl);


            user.setAnswer(userContent.substring(userContent.indexOf("回答")+2,userContent.indexOf("提问")-1));


            user.setStar(userContent.substring(userContent.indexOf("获得")+3,userContent.indexOf("次赞同")-1));

            user.setThanks(userContent.substring(userContent.lastIndexOf("获得")+3,userContent.indexOf("次感谢")-1));


            //关注的人
            String followingNum = userUrlContent.select(".NumberBoard-value").first().text();
            user.setFollowing(followingNum);

            //关注者数量
            String followersNum = userUrlContent.select(".NumberBoard-value").get(1).text();
            user.setFollowers(followersNum);


//            System.out.println(user.getCompany()+" "+user.getAnswer()+" "+user.getFollowers()+" "+user.getFollowing()+" "+user.getStar()+" "+user.getSex()+"\n");

//            System.out.println("执行saveuser");

            System.out.println(user.toString()+"\n");//输出对象

//            zhihuDao.saveUser(user);//保存用户信息到数据库


            //爬去用户关注人界面第一页的链接。
            int page_count = 1;
//
//            List<String> userFollowingUrl= page.getUrl().all();
//            for(int i=0;i<userFollowingUrl.size();i++){
//                userFollowingUrl.set(i,userFollowingUrl.get(i)+"/following?page=" + page_count);
//            }

            String userFollowingUrl= page.getUrl().get();

            page.addTargetRequest(userFollowingUrl.substring(0,userFollowingUrl.length()-8)+"/following?page=" + page_count);

//            System.out.println("获得关注界面url: "+userFollowingUrl);


        }


//        //传入用户关注列表，获取他所关注人的url
//        else if(page.getUrl().regex("https://www\\.zhihu\\.com/people/(.+)/following").match()){
//
//
////            System.out.print("获取关注人url中。。。。。   ");
//
//
//            Element userFollowingContent = null;
//            userFollowingContent = Jsoup.parse(page.getHtml().get());
//            Elements followingElements = userFollowingContent.select(".List-item");
//            //判断当前页关注人数是否为0，是的话就跳出循环
//            if (followingElements.size() != 0) {
//                for (Element e : followingElements) {
//                    String newUserUrl = e.select("a[href]").get(0).attr("href");
//                    //把获取到的地址加入队列
//                    if (!newUserUrl.contains("org")) {
//
//                        num.getAndIncrement();//用户数++
//
////                        String TargetRequest=newUserUrl+"/answers";
//                        page.addTargetRequest(newUserUrl+"/answers");
//
////                        System.out.print("获得关注人url："+TargetRequest);
//                    }
//                }
//            }
//        }


    }










    public Site getSite() {

        String[] ip = new String[4];
        try {
            while(proxyIpReader.readLine() != null) {
                String socket=proxyIpReader.readLine();
                String[] ipandport=socket.split(":");

                System.out.print(ipandport[0]+"  "+ipandport[1]);

                if(ipandport.length==2){
                    ip[0]="";
                    ip[1]="";
                    ip[2]=ipandport[0];
                    ip[3]=ipandport[1];
                    ipList.add(ip);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print(ipList);

        SimpleProxyPool simpleProxyPool=new SimpleProxyPool(ipList);

        this.site=this.site.setHttpProxyPool(ipList,false);



        return this.site;
    }

    public static void main(String[] args) {
        long startTime ,endTime;
        System.out.println("========知乎用户信息爬虫-启动ing！=========");
        startTime = new Date().getTime();
        //入口为：【https://www.zhihu.com/search?type=people&q=xxx 】，其中xxx 是搜索关键词
        Spider spider= Spider.create(new ZhiHuUserPageProcessor()).addUrl("https://www.zhihu.com/search?type=people&q="+keyword);
//
//        Spider spider= Spider.create(new ZhiHuUserPageProcessor()).addUrl("https://www.zhihu.com/people/jjmoe/answers");

        spider.thread(5);
        spider.run();
        endTime = new Date().getTime();
        System.out.println("========知乎用户信息小爬虫结束！===========");
        System.out.println("本次共得到"+num+"个用户数据----用时为："+(endTime-startTime)/1000+"s");
    }

}