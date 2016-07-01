package com.kocapplication.pixeleye.kockocapp.main.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2015-08-31.
 */
public class SearchAdapter  extends BaseAdapter{
    SearchActivity searchActivity;
    LayoutInflater inflater;
    ArrayList<viewHolder> viewArr;

    public SearchAdapter(SearchActivity searchActivity, ArrayList<String> result){
        viewArr = new ArrayList<>();
        this.searchActivity = searchActivity;
        inflater = (LayoutInflater)searchActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Keyword.str.size();
    }

    @Override
    public Object getItem(int position) {
        return Keyword.str.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int size = Keyword.str.size();
        viewHolder viewTemp = new viewHolder();
        convertView = inflater.inflate(R.layout.search_item, null);
        viewTemp.searchText = (TextView)convertView.findViewById(R.id.searchText);
        viewTemp.memberLayout = (LinearLayout)convertView.findViewById(R.id.memberLayout);

        viewTemp.searchText.setText(Keyword.str.get(size-(position+1)));
        viewTemp.memberLayout.setOnClickListener(buttonClickListener);
        viewTemp.memberLayout.setTag(position);

        return convertView;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.memberLayout:
                    int position = Integer.parseInt(v.getTag().toString());
                    Keyword.mainStr = Keyword.str.get(Keyword.str.size()-(position+1));
                    searchActivity.editText.setText(Keyword.mainStr);
                    break;
            }
        }
    };

    class viewHolder{
        public LinearLayout memberLayout;
        public TextView searchText;
    }
}
