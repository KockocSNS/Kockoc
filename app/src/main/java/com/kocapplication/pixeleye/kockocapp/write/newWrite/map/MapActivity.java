package com.kocapplication.pixeleye.kockocapp.write.newWrite.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.connect.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.EnterListener;
import com.kocapplication.pixeleye.kockocapp.util.map.GpsInfo;
import com.kocapplication.pixeleye.kockocapp.util.map.Item;
import com.kocapplication.pixeleye.kockocapp.util.map.OnFinishSearchListener;
import com.kocapplication.pixeleye.kockocapp.util.map.Searcher;

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

//주의 : 코드가 너무 복잡. 클래스 분리가 필요함.
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

    private RelativeLayout daumMapContainer;
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

        daumMapContainer = (RelativeLayout) containerView.findViewById(R.id.map_view_container);
        daumMap = new MapView(this);
        daumMap.setDaumMapApiKey(BasicValue.getInstance().getDAUM_MAP_API_KEY());
        daumMapContainer.addView(daumMap);

        recyclerView = (RecyclerView) containerView.findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(new ArrayList<Item>());
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
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem poiItem = new MapPOIItem();

        poiItem.setItemName("현재 위치");
        poiItem.setMapPoint(mapPoint);
        poiItem.setMarkerType(MapPOIItem.MarkerType.RedPin);
        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        daumMap.addPOIItem(poiItem);
        daumMap.setMapCenterPoint(mapPoint, true);

    }

    //맵 검색결과 없을때 토스트 띄울 handler
    public Handler searchFailHandler = new Handler() {

        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(MapActivity.this, "검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void searchMap(String text) {
//        MapPoint.GeoCoordinate geoCoordinate = daumMap.getMapCenterPoint().getMapPointGeoCoord();
//
//        double latitude = geoCoordinate.latitude; // 위도
//        double longitude = geoCoordinate.longitude; // 경도
//        int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1;

        String apiKey = BasicValue.getInstance().getDAUM_MAP_API_KEY();

        Searcher searcher = new Searcher(new SearchHandler());
        searcher.searchKeyword(getApplicationContext(), text, page, apiKey, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                //onSuccess 뿐만아니라 아래의 SearchHandler에도 Message가 전달된다.
                daumMap.removeAllPOIItems();
                showResults(itemList);

            }

            @Override
            public void onFail() {
                //토스트 출력
                searchFailHandler.sendEmptyMessage(0);
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }

    private void showResults(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();
        for (Item item : itemList) {
            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(itemList.indexOf(item));

            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);

            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            daumMap.addPOIItem(poiItem);
        }

        daumMap.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
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
            searchMap(text);
        }

        private void currentLocationClicked() {
            gps = new GpsInfo(MapActivity.this);

            if (!gps.isGetLocation()) {
                gps.showSettingAlertDialog();
                return;
            }

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            moveMap(latitude, longitude);

            Item item = new Item();
            item.title = "내 위치";
            item.phone = "";

            List<Item> items = new ArrayList<>();
            items.add(item);

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
        }
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.equals(hotel)) {
                searchMap("숙박");
            } else if (v.equals(food)) {
                searchMap("음식점");
            } else if (v.equals(mart)) {
                searchMap("마트");
            } else if (v.equals(terminal)) {
                searchMap("터미널");
            } else if (v.equals(hospital)) {
                searchMap("병원");
            }
        }
    }

    private class EditTextEnterListener extends EnterListener {
        @Override
        public void onEnter() {
            searchButton.performClick();
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);
            Item item = adapter.getItems().get(position);

            Intent intent = new Intent();
            intent.putExtra("LATITUDE", item.latitude);
            intent.putExtra("LONGITUDE", item.longitude);
            MapActivity.this.setResult(RESULT_OK, intent);
            MapActivity.this.finish();
        }
    }

    private class SearchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<Item> items = (ArrayList<Item>) msg.getData().getSerializable("THREAD");
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView detail;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.map_title);
            this.detail = (TextView) itemView.findViewById(R.id.map_detail_script);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Item> items;

        public RecyclerAdapter(List<Item> data) {
            super();
            if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
            items = data;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_map, parent, false);
            itemView.setOnClickListener(new ItemClickListener());
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            Item item = items.get(position);

            holder.title.setText(item.title);
            holder.detail.setText(item.phone);
        }



        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public List<Item> getItems() {
            return items;
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
