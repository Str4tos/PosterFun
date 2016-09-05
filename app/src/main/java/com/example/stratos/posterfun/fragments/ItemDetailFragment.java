package com.example.stratos.posterfun.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.stratos.posterfun.R;
import com.example.stratos.posterfun.utils.GenContent;
import com.example.stratos.posterfun.utils.PreCont;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ItemDetailFragment extends Fragment implements OnMapReadyCallback {

    public static final String ARG_ITEM_ID = "item_id";
    public static boolean likeFlag;

    private GenContent mItem;
    private TextView tvNumLike;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private View rootView;


    public ItemDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            int argItemId = getArguments().getInt(ARG_ITEM_ID);
            mItem = PreCont.ITEMS.get(argItemId);
            likeFlag = mItem.getLikeFlag();

        }
    }

    public void incNumLikes() {
        String numFav = mItem.getIncLikeNumber(tvNumLike.getRootView());
        if (!"".equals(numFav)) {
            tvNumLike.setText(Html.fromHtml(numFav + " &#x2764;"));
            likeFlag = mItem.getLikeFlag();
        }
    }


    private View.OnClickListener mButtonTicket = new View.OnClickListener() {
        public void onClick(View v) {
            Uri address = Uri.parse(mItem.getTicket_url());
            Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlinkIntent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);
        TextView tvName = (TextView) rootView.findViewById(R.id.detail_tvName);
        if (mItem.getAgelimit() != -1)
            tvName.setText(Html.fromHtml(
                    mItem.getName() + "<font color='#808080'> <i>" + mItem.getAgelimit() + "+</i></font>"));
        else tvName.setText(mItem.getName());

        ((TextView) rootView.findViewById(R.id.detail_tvStartView)).setText(mItem.getStart_view());
        StringBuilder strAddres = new StringBuilder();
        strAddres.append(mItem.getAddres()).append(",  ").append(mItem.getDistanceStr());
        ((TextView) rootView.findViewById(R.id.detail_tvAddres)).setText(strAddres);
        ((TextView) rootView.findViewById(R.id.detail_tvTerrain)).setText(String.format("%s: %s", mItem.getSity(), mItem.getTerrain()));
        ((TextView) rootView.findViewById(R.id.detail_tvDateTime)).setText(mItem.getDatetime());
        ((TextView) rootView.findViewById(R.id.detail_tvDescription)).setText(mItem.getDescription());

        tvNumLike = (TextView) rootView.findViewById(R.id.detail_tvLike);
        tvNumLike.setText(Html.fromHtml(mItem.getLikeNumber() + " &#x2764;"));

        //Цена и кнопка
        if ("0".equals(mItem.getPrice())) {
            ((TextView) rootView.findViewById(R.id.detail_tvPrice)).setText(
                    Html.fromHtml("<font color='#808080'>БЕСПЛАТНО</font>"));
        } else if ("-1".equals(mItem.getPrice()))
            rootView.findViewById(R.id.detail_tvPrice).setVisibility(View.GONE);
        else {
            rootView.findViewById(R.id.detail_PriceName).setVisibility(View.VISIBLE);
            Button mButtTicket = (Button) rootView.findViewById(R.id.detail_PriceButton);
            if (!" ".equals(mItem.getTicket_url())) {
                mButtTicket.setVisibility(View.VISIBLE);
                mButtTicket.setOnClickListener(mButtonTicket);
            }
            ((TextView) rootView.findViewById(R.id.detail_tvPrice)).setText(String.valueOf(mItem.getPrice() + " грн."));
        }
        ProgressBar progressBar = (ProgressBar) ItemDetailFragment.this.getActivity().findViewById(R.id.item_detail_progress);
        progressBar.setVisibility(View.GONE);

        mSupportMapFragment = (SupportMapFragment) ItemDetailFragment.this.getChildFragmentManager()
                .findFragmentById(R.id.detail_map);
        mSupportMapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng mLatLng = new LatLng(mItem.getLoclat(), mItem.getLoclng());
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(mLatLng).title(mItem.getTerrain()));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLatLng)
                .zoom(18)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


//        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

}
