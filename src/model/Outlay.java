package model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by shijia on 2015/4/11.
 */

@Table(name="tb_outlay")
public class Outlay {
    @Id(column="_id")
    private int id;

    @Column(column="year")
    private int year;

    @Column(column="month")
    private int month;

    @Column(column="day")
    private int day;

    @Column(column="time")
    private  long time;

    @Column(column="title")
    private String title;

    @Column(column="type_big")
    private String type_big;

    @Column(column="type_small")
    private String type_small;

    @Column(column="money")
    private  float money;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_big() {
        return type_big;
    }

    public void setType_big(String type_big) {
        this.type_big = type_big;
    }

    public String getType_small() {
        return type_small;
    }

    public void setType_small(String type_small) {
        this.type_small = type_small;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Outlay(int id, int year, int month, int day, long time, String title, String type_big, String type_small, float money) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.title = title;
        this.type_big = type_big;
        this.type_small = type_small;
        this.money = money;
    }
    public Outlay(){

    }
}
