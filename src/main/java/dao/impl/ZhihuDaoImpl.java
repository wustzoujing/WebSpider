package dao.impl;

import java.util.ArrayList;
import java.util.List;

import dao.ZhihuDao;
import entity.ZhihuUser;
import util.DBHelper;


/**
 * Created by zoujing on 2017/5/17.
 */
public class ZhihuDaoImpl implements ZhihuDao{
    public int saveUser(ZhihuUser user) {
        DBHelper dbhelper = new DBHelper();
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO spider_zhihu_user (name,identity,sex,profession,company,position,school,major,picUrl,star,thanks,answer,following,followers)")
                //`key`,`name`,identity,location,profession,sex,school,major,recommend,picUrl,agree,thanks,ask,answer,article,collection
                .append("VALUES (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ");
        //设置 sql values 的值
        List<String> sqlValues = new ArrayList();

        sqlValues.add(user.getName());
        sqlValues.add(user.getIdentity());
        sqlValues.add(""+user.getSex());
        sqlValues.add(user.getProfession());

        sqlValues.add(user.getCompany());
        sqlValues.add(user.getPosition());

        sqlValues.add(user.getSchool());
        sqlValues.add(user.getMajor());

        sqlValues.add(user.getPicUrl());

        sqlValues.add(user.getStar());
        sqlValues.add(user.getThanks());

        sqlValues.add(user.getAnswer());
        sqlValues.add(user.getFollowers());
        sqlValues.add(user.getFollowing());
        int result = dbhelper.executeUpdate(sql.toString(), sqlValues);
        return result;
    }
}
