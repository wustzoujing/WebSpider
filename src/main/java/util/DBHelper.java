package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * Created by zoujing on 2017/5/17.
 */
public class DBHelper {
   // public static final String driver_class = "com.mysql.jdbc.Driver";
    public static final String driver_url = "jdbc:mysql://localhost:3306/sys?useunicode=true&characterEncoding=utf8";
    public static final String user = "root";
    public static final String password = "123";

    private static Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rst = null;
    /**
     * Connection
     */
    public DBHelper() {
        try {
            conn = DBHelper.getConnInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例模式
     * 线程同步
     * @return
     */
    private static synchronized Connection getConnInstance() {
        if(conn == null){
            try {
                //Class.forName(driver_class);
                conn = DriverManager.getConnection(driver_url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("连接数据库成功");
        }
        return conn;
    }


    /**
     * close
     */
    public void close() {

        try {
            if (conn != null) {
                DBHelper.conn.close();
            }
            if (pst != null) {
                this.pst.close();
            }
            if (rst != null) {
                this.rst.close();
            }
            System.out.println("关闭数据库成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * query
     *
     * @param sql
     * @param sqlValues
     * @return ResultSet
     */
    public ResultSet executeQuery(String sql, List<String> sqlValues) {
        try {
            pst = conn.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(pst, sqlValues);
            }
            rst = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rst;
    }

    /**
     * update
     *
     * @param sql
     * @param sqlValues
     * @return result
     */
    public int executeUpdate(String sql, List<String> sqlValues) {
        int result = -1;
        try {
            pst = conn.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(pst, sqlValues);
            }
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * sql set value
     *
     * @param pst
     * @param sqlValues
     */
    private void setSqlValues(PreparedStatement pst, List<String> sqlValues) {
        for (int i = 0; i < sqlValues.size(); i++) {
            try {
                pst.setObject(i + 1, sqlValues.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

