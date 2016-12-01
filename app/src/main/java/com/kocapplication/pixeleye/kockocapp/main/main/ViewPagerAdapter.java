package com.kocapplication.pixeleye.kockocapp.main.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailActivity;

/**
 * Created by Hyeongpil on 2016-10-13.
 */
public class ViewPagerAdapter extends PagerAdapter{
    final static String TAG = "ViewPagerAdapter";
    private LayoutInflater mInflater;
    private int itemCount;
    private Context mContext;

    // imageResources에 사진들 넣고 MainFragment에서 아이템 카운트 맞추기 // 서버에서 가져온 갯수 만큼 itemcount 수정해야함
    private int[] imageResources = {
            R.drawable.main_image0,
            R.drawable.main_image11,
//            R.drawable.main_image2
    };

    public ViewPagerAdapter(Context mContext, int itemCount) {
        super();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.itemCount = itemCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int realPosition = position % itemCount; // 전체 뷰 갯수로 나눠줌 (뷰페이저 순환을 위해)
        View v = mInflater.inflate(R.layout.main_viewpager_item,null);

        ImageView img = (ImageView) v.findViewById(R.id.iv_main_frag_viewpager_item);
        img.setImageResource(imageResources[realPosition]);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                switch (realPosition){
                    case 0 :
                        intent.putExtra("boardNo",504);
                        intent.putExtra("courseNo",377);
                        intent.putExtra("board_userNo",235);
                        mContext.startActivity(intent);
                        break;
                    case 1 :
                        intent.putExtra("boardNo",471);
                        intent.putExtra("courseNo",374);
                        intent.putExtra("board_userNo",93);
                        mContext.startActivity(intent);
                        break;
//                    case 2 : Log.e(TAG,"클릭2");
//                        break;
                }
            }
        });

        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public int getRealCount(){
        return itemCount;
    }
}
