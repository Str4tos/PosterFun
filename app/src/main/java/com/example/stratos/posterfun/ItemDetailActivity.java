package com.example.stratos.posterfun;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.stratos.posterfun.fragments.ItemDetailFragment;
import com.example.stratos.posterfun.utils.PreCont;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {
    private ItemDetailFragment fragment;
    private FloatingActionButton fab;
    //private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final int itemID = getIntent().getIntExtra(ItemDetailFragment.ARG_ITEM_ID, 1);

        fab = (FloatingActionButton) findViewById(R.id.fab_like);
        if (fab != null) {
            if (!PreCont.ITEMS.get(itemID).getLikeFlag()) {
                fab.setVisibility(FloatingActionButton.GONE);
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.incNumLikes();
                    fab.setVisibility(FloatingActionButton.GONE);
                }
            });

        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(PreCont.ITEMS.get(itemID).getName());
            appBarLayout.setCollapsedTitleTextColor(Color.WHITE);
        }

        PreCont.loadImagePicasso(PreCont.ITEMS.get(itemID).getImage(), (ImageView) findViewById(R.id.detail_image));

        //progressBar = (ProgressBar) findViewById(R.id.item_detail_progress);


        if (savedInstanceState == null) {
            new Thread(new Runnable() {
                public void run() {
                    Bundle arguments = new Bundle();
                    arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, itemID);
                    fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.item_detail_container, fragment)
                            .commit();
                }
            }).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
