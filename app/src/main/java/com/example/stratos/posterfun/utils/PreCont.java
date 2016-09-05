package com.example.stratos.posterfun.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;

import com.example.stratos.posterfun.R;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PreCont {

    public static List<GenContent> ITEMS;
    private boolean flagFirstWhere;

    public static final String ARG_SECTION_CITY = "section_sity";
    public static final String ARG_SECTION_CAT = "section_cat";
    public static final String ARG_SECTION_TAB = "section_tab";
    public static final String ARG_SECTION_SORT = "section_sort";
    public static final String SELECT_ALL = "Все";

    public static String filter_catName;
    public static String filter_cityName;
    public static int filter_costVal;
    public static int filter_distVal;
    public static int filter_sort;
    public static boolean flagCreate;
    public static int offset;

    public PreCont(Activity activity) {
        if (ITEMS == null) {
            new MyLocations(activity);
            filter_catName = SELECT_ALL;
            filter_cityName = new PrefStor(activity).getCityName();
            filter_costVal = -1;
            filter_distVal = -1;
            filter_sort = 0;
            flagCreate = true;
            offset = 0;

            //Начальная настройка Picasso
            Picasso.Builder builder = new Picasso.Builder(activity);
            builder.downloader(new OkHttpDownloader(activity));
            Picasso built = builder.build();
            //built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
        }

        if(flagCreate) {
            ITEMS = new ArrayList<>();
            setITEMS(activity);
            MyLocations.setItemsDistance(ITEMS);
            flagCreate = false;
        }

        switch (PreCont.filter_sort) {
            case 1:
                setSortPrice();
                break;
            case 2:
                setSortLike();
                break;
            case 3:
                setSortDistance();
                break;
            default:
                setSortDate();
                break;
        }

    }

    private void setITEMS(Context context) {

        flagFirstWhere = true;

        //Открываем БД
        DBContent dbContent = new DBContent(context);
        SQLiteDatabase sqLiteDatabase = dbContent.getWritableDatabase();

        StringBuilder sSelect = new StringBuilder();
        sSelect.append("SELECT ev.*, lc.addres, lc.terrain, lc.loclat, lc.loclng, ct.city")
                .append(" FROM eventlist ev")
                .append(" JOIN locals lc ON lc.id = ev.locale")
                .append(" JOIN city ct ON ct.id = lc.city");
        if (!filter_catName.equals(SELECT_ALL))
            sSelect.append(" JOIN eventincat evic ON evic.idevent = ev.id")
                    .append(" JOIN catevent ON catevent.id = evic.idcat").append(getWhere())
                    .append(" catevent.cat='").append(filter_catName).append("'");
        if (!filter_cityName.equals(SELECT_ALL))
            sSelect.append(getWhere()).append(" ct.city='").append(filter_cityName).append("'");
        if (filter_costVal == 0)
            sSelect.append(getWhere()).append(" ev.pricestart < 0");
        else if (filter_costVal > 0)
            sSelect.append(getWhere()).append(" ev.pricestart < ").append(filter_costVal);
//        if(ItemListActivity.filter_sort == 1)
//            sSelect.append(" ORDER BY ev.pricestart");
//        else
        sSelect.append(" ORDER BY ev.id DESC");

        sSelect.append(" LIMIT 10");
        if (offset > 0) {
            sSelect.append(" OFFSET ").append(offset);
        }
        Cursor CurITEMS = sqLiteDatabase.rawQuery(sSelect.toString(), null);
        //Заполнение List<GenContent> ITEMS
        if (CurITEMS != null) {
            if (CurITEMS.moveToFirst()) {
                do {
                    final int curID = CurITEMS.getInt(0);
                    int likes = 0;
                    Cursor curFavorit = sqLiteDatabase.rawQuery(
                            "SELECT favorite.number FROM favorite WHERE (favorite.idevent = " + curID + ")", null);
                    if (curFavorit != null) {
                        curFavorit.moveToFirst();
                        likes = curFavorit.getInt(0);
                        curFavorit.close();
                    }

                    ITEMS.add(new GenContent(
                            curID,
                            CurITEMS.getString(1),
                            CurITEMS.getInt(2),
                            CurITEMS.getString(3),
                            getDateTimeCombine(CurITEMS.getString(4),
                                    CurITEMS.getString(5),
                                    CurITEMS.getString(6),
                                    CurITEMS.getString(7)),
                            CurITEMS.getString(8),
                            getPriceCombine(CurITEMS.getInt(12), CurITEMS.getInt(13)),
                            CurITEMS.getInt(12),
                            CurITEMS.getString(9),
                            CurITEMS.getString(10),
                            getCategoryCombine(CurITEMS.getInt(0), sqLiteDatabase),
                            CurITEMS.getString(14),
                            CurITEMS.getString(15),
                            CurITEMS.getDouble(16),
                            CurITEMS.getDouble(17),
                            CurITEMS.getString(18),
                            likes
                    ));

                } while (CurITEMS.moveToNext());
            }
            CurITEMS.close();
        }
        dbContent.close();

    }

    private void setSortDistance() {
        Collections.sort(ITEMS, new Comparator<GenContent>() {
            @Override
            public int compare(GenContent lhs, GenContent rhs) {
                return lhs.getDistance() < rhs.getDistance() ? -1 : lhs.getDistance() == rhs.getDistance() ? 0 : 1;
            }
        });
    }

    private void setSortDate() {
        Collections.sort(ITEMS, new Comparator<GenContent>() {
            @Override
            public int compare(GenContent lhs, GenContent rhs) {
                return lhs.getId() > rhs.getId() ? -1 : lhs.getId() == rhs.getId() ? 0 : 1;
            }
        });
    }

    private void setSortPrice() {
        Collections.sort(ITEMS, new Comparator<GenContent>() {
            @Override
            public int compare(GenContent lhs, GenContent rhs) {
                return lhs.getPriceforsort() < rhs.getPriceforsort() ? -1 : lhs.getPriceforsort() == rhs.getPriceforsort() ? 0 : 1;
            }
        });
    }

    private void setSortLike() {
        Collections.sort(ITEMS, new Comparator<GenContent>() {
            @Override
            public int compare(GenContent lhs, GenContent rhs) {
                return lhs.getLikes() > rhs.getLikes() ? -1 : lhs.getLikes() == rhs.getLikes() ? 0 : 1;
            }
        });
    }

    private String getWhere() {
        if (flagFirstWhere) {
            flagFirstWhere = false;
            return " WHERE";
        }
        return " AND";
    }

    private String getDateTimeCombine(String dateStart, String timeStart, String dateEnd, String timeEnd) {
        StringBuilder res = new StringBuilder();
        if (!" ".equals(dateStart))
            res.append(dateStart).append(", ");
        res.append(timeStart);
        if (!" ".equals(timeEnd)) {
            res.append(" - ");
            if (!" ".equals(dateEnd)) {
                res.append(dateEnd).append(", ");
            }
            res.append(timeEnd);
        }
        return res.toString();
    }

    private String getPriceCombine(int startPrice, int endPrice) {
        if (endPrice != 0) {
            return startPrice + "-" + endPrice;
        }
        return startPrice + "";
    }

    private String getCategoryCombine(int idEvent, SQLiteDatabase sqLiteDatabase) {
        StringBuilder res = new StringBuilder();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT catevent.cat FROM eventincat JOIN catevent ON catEvent.id = eventincat.idcat " +
                "JOIN eventlist ON eventlist.id = eventincat.idevent " +
                "WHERE (((eventlist.id)=" + idEvent + "));", null);
        if (cursor == null) return " ";
        if (cursor.moveToFirst()) {
            res.append(cursor.getString(0));
            while (cursor.moveToNext()) {
                res.append(",").append(cursor.getString(0));
            }
        }
        return res.toString();
    }

    public static String getFiltersStr(Activity activity){
        StringBuilder res = new StringBuilder("Фильтры");
        if (!filter_catName.equals(SELECT_ALL))
            res.append("|").append(filter_catName);
        if (filter_costVal == 0)
            res.append("|Только бесплатные");
        if (filter_costVal > 0)
            res.append("|Дешевле:").append(filter_costVal).append("грн.");
        if (filter_distVal > 0)
            res.append("|Радиус:").append(filter_distVal).append("км");
        if (filter_sort > 0) {
            String[] sortName = activity.getResources().getStringArray(R.array.fsort);
            res.append("|").append(sortName[filter_sort]);
        }
        return res.toString();
    }

    public static void loadImagePicasso(String uri, ImageView imageView) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.load_image)
                .into(imageView);
    }

}
