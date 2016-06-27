package com.kocapplication.pixeleye.kockocapp.write.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.EnterListener;
import com.kocapplication.pixeleye.kockocapp.util.map.GpsInfo;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han_ on 2016-06-27.
 */
public class MapActivity extends BaseActivityWithoutNav
        implements MapView.MapViewEventListener, MapView.POIItemEventListener {
    private final String TAG = "MAP_ACTIVITY";

    private Button currentLocation;
    private EditText searchText;
    private Button searchButton;

    private Button hotel;
    private Button food;
    private Button mart;
    private Button terminal;
    private Button hospital;

    private MapView daumMap;
    private GpsInfo gps;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        View titleView = getLayoutInflater().inflate(R.layout.actionbar_text_title, null);
        TextView title = (TextView) titleView.findViewById(R.id.actionbar_text_title);
        title.setText("지도");
        actionBarTitleSet(titleView);

        container.setLayoutResource(R.layout.activity_map);
        View containerView = container.inflate();

        currentLocation = (Button) containerView.findViewById(R.id.current_location);
        searchText = (EditText) containerView.findViewById(R.id.search_text);
        searchButton = (Button) containerView.findViewById(R.id.search_button);

        hotel = (Button) containerView.findViewById(R.id.map_hotels);
        food = (Button) containerView.findViewById(R.id.map_foods);
        mart = (Button) containerView.findViewById(R.id.map_mart);
        terminal = (Button) containerView.findViewById(R.id.map_terminal);
        hospital = (Button) containerView.findViewById(R.id.map_hospital);

        daumMap = (MapView) containerView.findViewById(R.id.map_view);
        daumMap.setDaumMapApiKey(BasicValue.getInstance().getDAUM_MAP_API_KEY());

        recyclerView = (RecyclerView) containerView.findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(new ArrayList<String>());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);

        listenerSet();
    }

    private void listenerSet() {
        View.OnClickListener upperButtonListener = new UpperButtonListener();
        currentLocation.setOnClickListener(upperButtonListener);
        searchButton.setOnClickListener(upperButtonListener);

        View.OnClickListener buttonListener = new ButtonListener();
        hotel.setOnClickListener(buttonListener);
        food.setOnClickListener(buttonListener);
        mart.setOnClickListener(buttonListener);
        terminal.setOnClickListener(buttonListener);
        hospital.setOnClickListener(buttonListener);

        searchText.setOnEditorActionListener(new EditTextEnterListener());
    }

    private void moveMap(double latitude, double longitude) {
        MapPointBounds bounds = new MapPointBounds();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem poiItem = new MapPOIItem();

        poiItem.setItemName("현재 위치");
        poiItem.setMapPoint(mapPoint);
        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//        poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
//        poiItem.setCustomImageAutoscale(false);
//        poiItem.setCustomImageAnchor(0.5f, 1.0f);

        daumMap.addPOIItem(poiItem);
        daumMap.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds));
    }

    private class UpperButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(searchButton)) {
                searchButtonClicked();
            } else if (v.equals(currentLocation)) {
                currentLocationClicked();
            }
        }

        private void searchButtonClicked() {
            String text = searchText.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(MapActivity.this, "검색할 장소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            MapPoint.GeoCoordinate geoCoordinate = daumMap.getMapCenterPoint().getMapPointGeoCoord();
            double latitude = geoCoordinate.latitude;
            double longitude = geoCoordinate.longitude;
            int radius = 10000;
            int page = 1;



            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

        }

        private void currentLocationClicked() {
            gps = new GpsInfo(MapActivity.this);

            if (!gps.isGetLocation()) {
                gps.showSettingAlertDialog();
                return;
            }

            Log.i(TAG, "gps 사용");

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Log.i(TAG, latitude + "");
            Log.i(TAG, longitude + "");

            moveMap(latitude, longitude);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
        }
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class EditTextEnterListener extends EnterListener {
        @Override
        public void onEnter() {
            searchButton.performClick();
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {


        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<String> items;

        public RecyclerAdapter(List<String> data) {
            super();
            if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
            items = data;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }


}
