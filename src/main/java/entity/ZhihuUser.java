package entity;

/**
 * Created by zoujing on 2017/5/17.
 */
public class ZhihuUser {
    private String name;//用户名
    private String identity;//身份
    private String position;//所在地

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    private String profession;//行业
    private int sex;//性别

    public String getName() {
        return name;
    }

    public String getIdentity() {
        return identity;
    }

    public String getProfession() {
        return profession;
    }

    public int getSex() {
        return sex;
    }

    public String getSchool() {
        return school;
    }

    public String getMajor() {
        return major;
    }

    public String getCompany() {
        return company;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getStar() {
        return star;
    }

    public String getThanks() {
        return thanks;
    }

    public String getAnswer() {
        return answer;
    }

    public String getFollowing() {
        return following;
    }

    public String getFollowers() {
        return followers;
    }

    private String school;//学校
    private String major;//专业
    private String company;//个人简介
    private String picUrl;//头像url
    private String star;//收藏
    private String thanks;//感谢
    private String answer;//回答数
    private String following;//文章数
    private String followers;//收藏数

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setThanks(String thanks) {
        this.thanks = thanks;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "ZhihuUser [name=" + name + ", identity=" + identity + ", position=" + position + ", profession="
                + profession + ", sex=" + sex + ", school=" + school + ", major=" + major + ", company=" + company
                + ", picUrl=" + picUrl + ", star=" + star + ", thanks=" + thanks +  ", answer="
                + answer + ", following=" + following + ", followers=" + followers + "]";
    }
}
