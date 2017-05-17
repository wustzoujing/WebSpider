package entity;

/**
 * Created by zoujing on 2017/5/17.
 */
public class ZhihuUser {
    private String key;//keyword
    private String name;//用户名
    private String identity;//身份
    private String location;//所在地
    private String profession;//行业
    private int sex;//性别
    private String school;//学校
    private String major;//专业
    private String recommend;//个人简介
    private String picUrl;//头像url
    private int agree;//赞同
    private int thanks;//感谢
    private int ask;//提问数
    private int answer;//回答数
    private int article;//文章数
    private int collection;//收藏数


    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public String getRecommend() {
        return recommend;
    }
    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
    public String getPicUrl() {
        return picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public int getAgree() {
        return agree;
    }
    public void setAgree(int agree) {
        this.agree = agree;
    }
    public int getThanks() {
        return thanks;
    }
    public void setThanks(int thanks) {
        this.thanks = thanks;
    }
    public int getAsk() {
        return ask;
    }
    public void setAsk(int ask) {
        this.ask = ask;
    }
    public int getAnswer() {
        return answer;
    }
    public void setAnswer(int answer) {
        this.answer = answer;
    }
    public int getArticle() {
        return article;
    }
    public void setArticle(int article) {
        this.article = article;
    }
    public int getCollection() {
        return collection;
    }
    public void setCollection(int collection) {
        this.collection = collection;
    }
    @Override
    public String toString() {
        return "ZhihuUser [name=" + name + ", identity=" + identity + ", location=" + location + ", profession="
                + profession + ", sex=" + sex + ", school=" + school + ", major=" + major + ", recommend=" + recommend
                + ", picUrl=" + picUrl + ", agree=" + agree + ", thanks=" + thanks + ", ask=" + ask + ", answer="
                + answer + ", article=" + article + ", collection=" + collection + "]";
    }
}
