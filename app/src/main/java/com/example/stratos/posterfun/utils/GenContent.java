package com.example.stratos.posterfun.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.view.View;

public class GenContent {

    private final int id; //0
    private final String name; //1
    private final int agelimit; //2
    private final String image; //3
    private final String datetime; //4+5+6+7
    private final String description; //8
    private final String price; //9
    private final int priceforsort;
    private final String start_view; //10
    private final String ticket_url; //11
    private final String category; //catevent
    private final String addres; //locals
    private final String terrain; //locals
    private final double loclat; //locals
    private final double loclng; //locals
    private final String sity; //locals

    private int likes;
    private boolean like_flag;

    private float distance;


    public GenContent(int id,
                      String name,
                      int agelimit,
                      String image,
                      String datetime,
                      String description,
                      String price,
                      int priceforsort,
                      String start_view,
                      String ticket_url,
                      String category,
                      String addres,
                      String terrain,
                      double loclat,
                      double loclng,
                      String sity,
                      int likes) {
        this.id = id;
        this.name = name;
        this.agelimit = agelimit;
        this.image = image;
        this.datetime = datetime;
        this.description = description;
        this.price = price;
        this.priceforsort = priceforsort;
        this.start_view = start_view;
        this.ticket_url = ticket_url;
        this.category = category;
        this.addres = addres;
        this.terrain = terrain;
        this.loclat = loclat;
        this.loclng = loclng;
        this.sity = sity;
        this.likes = likes;
        this.distance = 0;
        this.like_flag = true;
    }

    public String getLikeNumber() {
        return String.valueOf(likes);
    }

    public String getIncLikeNumber(View view) {
        if (!like_flag) {
            Snackbar.make(view, "Даное действие можно выполнить только один раз", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return "";
        }
        DBContent dbContent = new DBContent(view.getContext());
        SQLiteDatabase sqLiteDatabase = dbContent.getWritableDatabase();
        ContentValues cv = new ContentValues(2);
        cv.put("number", ++likes);
        if (likes == 1) {
            cv.put("idevent", id);
            sqLiteDatabase.insert("favorite", null, cv);
        } else
            sqLiteDatabase.update("favorite", cv, "idevent = ?", new String[]{String.valueOf(id)});

        dbContent.close();
        this.like_flag = false;
        return String.valueOf(likes);
    }

    public boolean getLikeFlag() {
        return like_flag;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getDistanceStr() {
        if (distance < 1)
            return "До места:" + distance * 1000 + "м";
        int result = (int) distance;
        return "До места:" + result + "км";
    }

    public int getId() {
        return id;
    }

    public int getPriceforsort() {
        return priceforsort;
    }

    public int getLikes() {
        return likes;
    }

    public double getLoclat() {
        return loclat;
    }

    public double getLoclng() {
        return loclng;
    }

    public int getAgelimit() {
        return agelimit;
    }

    public String getAddres() {
        return addres;
    }

    public String getCategory() {
        return category;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getSity() {
        return sity;
    }

    public String getStart_view() {
        return start_view;
    }

    public String getTerrain() {
        return terrain;
    }

    public String getTicket_url() {
        return ticket_url;
    }
}