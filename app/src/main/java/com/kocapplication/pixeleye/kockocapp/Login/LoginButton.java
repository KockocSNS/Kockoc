package com.kocapplication.pixeleye.kockocapp.login;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kocapplication.pixeleye.kockocapp.R;

/**
 * Created by Han_ on 2016-04-01.
 */
public class LoginButton extends LinearLayout {
    private LinearLayout background;
    private ImageView symbol;
    private TextView text;

    public LoginButton(Context context) {
        super(context);
        initView();
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_button_custom, this, false);
        addView(view);

        background = (LinearLayout) findViewById(R.id.background);
        symbol = (ImageView) findViewById(R.id.symbol);
        text = (TextView) findViewById(R.id.text);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton, defStyle, 0);
        setTypeArray(typedArray);
    }


    private void setTypeArray(TypedArray typedArray) {
        int bg_resID = typedArray.getResourceId(R.styleable.LoginButton_bg, R.drawable.background_button_login);
        background.setBackgroundResource(bg_resID);

        int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_symbol, 0);
        symbol.setImageResource(symbol_resID);
        if (symbol_resID == 0) {
            symbol.setVisibility(GONE);
        }

        int textColor = typedArray.getColor(R.styleable.LoginButton_textColor, 0);
        text.setTextColor(textColor);

        String text_string = typedArray.getString(R.styleable.LoginButton_text);
        text.setText(text_string);

        typedArray.recycle();
    }

    public void setBackground(int background_resID) {
        background.setBackgroundResource(background_resID);
    }

    public void setSymbol(int symbol_resID) {
        symbol.setImageResource(symbol_resID);
    }

    public void setTextColor(int color) {
        text.setTextColor(color);
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
