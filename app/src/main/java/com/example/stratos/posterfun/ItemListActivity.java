package com.example.stratos.posterfun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stratos.posterfun.fragments.ItemDetailFragment;
import com.example.stratos.posterfun.utils.GenContent;
import com.example.stratos.posterfun.utils.PreCont;
import com.example.stratos.posterfun.utils.PrefStor;

import java.util.List;

public class ItemListActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean mTwoPane;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView tvShowFilts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getTitle());
        }

        tvShowFilts = (TextView) findViewById(R.id.showfilts);
        if (tvShowFilts != null) {
            tvShowFilts.setOnClickListener(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new PreCont(this);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        tvShowFilts.setText(PreCont.getFiltersStr(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, FiltersActivity.class);
        intent.putExtra(PreCont.ARG_SECTION_CAT, PreCont.filter_catName);
        intent.putExtra(PreCont.ARG_SECTION_CITY, PreCont.filter_cityName);
        intent.putExtra(PreCont.ARG_SECTION_SORT, PreCont.filter_sort);
        switch (item.getItemId()) {
            case R.id.menu_fdate:
                intent.putExtra(PreCont.ARG_SECTION_TAB, 1);
                break;
            case R.id.menu_fother:
                intent.putExtra(PreCont.ARG_SECTION_TAB, 2);
                break;
            case R.id.menu_fsort:
                intent.putExtra(PreCont.ARG_SECTION_TAB, 3);
                break;
            case R.id.menu_fcity:
                intent.putExtra(PreCont.ARG_SECTION_TAB, 4);
                break;
        }
        this.startActivityForResult(intent, 1);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final String catName = data.getStringExtra(PreCont.ARG_SECTION_CAT);
        if (catName != null) {
            PreCont.filter_catName = catName;
            PreCont.flagCreate = true;
        }
        final String cityName = data.getStringExtra(PreCont.ARG_SECTION_CITY);
        if (cityName != null) {
            PreCont.filter_cityName = cityName;
            PreCont.flagCreate = true;
            new PrefStor(this).saveCityName(cityName);
        }
        final int sort = data.getIntExtra(PreCont.ARG_SECTION_SORT, 0);
        PreCont.filter_sort = sort;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(PreCont.ITEMS));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Нажмите BACK еще раз для выхода", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FiltersActivity.class);
        intent.putExtra(PreCont.ARG_SECTION_CAT, PreCont.filter_catName);
        intent.putExtra(PreCont.ARG_SECTION_CITY, PreCont.filter_cityName);
        intent.putExtra(PreCont.ARG_SECTION_SORT, PreCont.filter_sort);
        this.startActivityForResult(intent, 1);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<GenContent> gITEMS;

        public SimpleItemRecyclerViewAdapter(List<GenContent> items) {
            gITEMS = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            GenContent holdITEM = gITEMS.get(position);
            PreCont.loadImagePicasso(holdITEM.getImage(), holder.vImage);

            holder.vName.setText(
                    Html.fromHtml(
                            holdITEM.getName() + "<font color='#808080'><small><i> "
                                    + holdITEM.getCategory() + "</i></small></font>"));

            switch (PreCont.filter_sort) {
                case 1: //стоимость
                    if ("0".equals(holdITEM.getPrice())) {
                        holder.vAdditionally.setText("БЕСПЛАТНО");
                    } else if ("-1".equals(holdITEM.getPrice()))
                        holder.vAdditionally.setText(holdITEM.getTerrain());
                    else {
                        holder.vAdditionally.setText(String.valueOf(holdITEM.getPrice() + " грн."));
                    }
                    break;
                case 3: //растояние
                    holder.vAdditionally.setText(holdITEM.getDistanceStr());
                    break;
                default: //Дата
                    if (PreCont.filter_cityName.equals(PreCont.SELECT_ALL))
                        holder.vAdditionally.setText(holdITEM.getSity());
                    else holder.vAdditionally.setText(holdITEM.getTerrain());
                    break;
            }


            holder.vDateTime.setText(holdITEM.getDatetime());
            holder.vLike.setText(holdITEM.getLikeNumber());
            holder.vLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String numFav = gITEMS.get(position).getIncLikeNumber(holder.vLike.getRootView());
                    if (!"".equals(numFav))
                        holder.vLike.setText(numFav);
                }
            });

            holder.genView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, position);
                        //arguments.putString(,  /*holder.tempItem.name*/);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, position);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return gITEMS.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView vName;
            public final TextView vAdditionally;
            public final ImageView vImage;
            public final TextView vDateTime;
            public final TextView vLike;
            public final ImageButton vLikeBtn;
            public final View genView;

            public ViewHolder(View view) {
                super(view);
                genView = view;
                vName = (TextView) view.findViewById(R.id.list_Name);
                vLike = (TextView) view.findViewById(R.id.list_likeNum);
                vLikeBtn = (ImageButton) view.findViewById(R.id.list_likeBtn);
                vAdditionally = (TextView) view.findViewById(R.id.list_Additionally);
                vImage = (ImageView) view.findViewById(R.id.list_image);
                vDateTime = (TextView) view.findViewById(R.id.list_DateTime);
            }

        }
    }
}
