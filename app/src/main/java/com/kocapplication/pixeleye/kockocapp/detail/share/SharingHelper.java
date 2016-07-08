package com.kocapplication.pixeleye.kockocapp.detail.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;

import java.util.List;

/**
 * Created by Han on 2016-07-08.
 */
public class SharingHelper {
    private Context context;
    private RecyclerView recyclerView;
    private ShareAdapter adapter;
    private DetailPageData data;

    private final String kakaoPackage = "com.kakao.talk";
    private final String facebookPackage = "com.facebook.katana";
    private final String instagramPackage = "com.instagram.android";
    private final String bandPackage = "com.nhn.band";
    private final String googleMailPackage = "com.google.android.gm";

    public SharingHelper(Context context, DetailPageData data) {
        this.context = context;
        this.data = data;
    }

    public List<ResolveInfo> checkSharableApp() throws NullPointerException {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfo.isEmpty()) {
            Toast.makeText(context, "공유 가능한 앱이 없습니다.", Toast.LENGTH_SHORT).show();
            return null;
        }

        for (int i = 0; i < resolveInfo.size(); i++) {
            String packageName = resolveInfo.get(i).activityInfo.packageName;

            if (packageName.equalsIgnoreCase(kakaoPackage) || packageName.equalsIgnoreCase(facebookPackage) || packageName.equalsIgnoreCase(instagramPackage)
                    || packageName.equalsIgnoreCase(bandPackage) || packageName.equalsIgnoreCase(googleMailPackage)) {
                continue;
            } else resolveInfo.remove(i);
        }

        return resolveInfo;
    }

    public void showShareDialog(List<ResolveInfo> shareableApps) {
        if (shareableApps == null) shareableApps = checkSharableApp();

        recyclerView = new RecyclerView(context);
        adapter = new ShareAdapter(context, shareableApps, new ShareButtonClickListener());

        GridLayoutManager manager = new GridLayoutManager(context, 3, LinearLayoutManager.HORIZONTAL, false);
        manager.scrollToPosition(0);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("공유하기");
        builder.setView(recyclerView);
        builder.create().show();
    }

    public static void sharedByOtherApp(Activity activity, ShareReceiveInterface receive) {
        Intent intent = activity.getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (action == null || type == null) return;

        if (action.equals(Intent.ACTION_SEND)) {

            if (type.equals("text/plain")) receive.shareText(intent);
            else if (type.startsWith("image/")) receive.shareImage(intent);

        } else if (action.equals(Intent.ACTION_SEND_MULTIPLE)) {

            if (type.startsWith("image/")) receive.shareImage(intent);

        } else {
            receive.shareText(intent);
        }
    }

    private class ShareButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);

            String packageName = adapter.getItems().get(position).activityInfo.packageName;

                  if (packageName.equalsIgnoreCase(kakaoPackage))       kakaoShare();
            else if (packageName.equalsIgnoreCase(facebookPackage))    facebookShare();
            else if (packageName.equalsIgnoreCase(instagramPackage))   instagramShare();
            else if (packageName.equalsIgnoreCase(bandPackage))         bandShare();
            else if (packageName.equalsIgnoreCase(googleMailPackage))  googleMailShare();
            else otherShare();
        }
    }

    private void kakaoShare() {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
            KakaoTalkLinkMessageBuilder messageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            messageBuilder.addText(data.getBoardText());
            messageBuilder.addImage("http://221.160.54.160:8080/board_image/" + data.getUserNo() + "/" + data.getBoardImgArr().get(0), 320, 320);
            messageBuilder.addAppButton("앱으로 이동",
                    new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder
                                    .createAndroidActionInfoBuilder()
                                    .setExecuteParam("boardNo=" + data.getBoardNo() + "&courseNo=" + data.getCourseNo())
                                    .setMarketParam("referrer=kakaotalklink")
                                    .build())
                            .addActionInfo(AppActionInfoBuilder
                                    .createiOSActionInfoBuilder()
                                    .setExecuteParam("execparamkey1=1111")
                                    .build())
                            .build())
                    .build();
            kakaoLink.sendMessage(messageBuilder, context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void facebookShare() {
        FacebookSdk.sdkInitialize(context);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog((Activity) context);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {}

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) { }
        });

        if (shareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder().
                    setContentTitle(data.getBoardText()).
                    setContentDescription(data.getBoardText()).
                    setContentUrl(Uri.parse("http://www.kockoc.net")).
                    setImageUrl(Uri.parse("http://221.160.54.160:8080/board_image/" + data.getUserNo() + "/" + data.getBoardImgArr().get(0))).
                    build();
            shareDialog.show(linkContent);
        }
    }

    private void instagramShare() {

    }

    private void bandShare() {
        Uri uri = Uri.parse("bandapp://create/post?text=test&route=www.google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void googleMailShare() {

    }

    private void otherShare() {
    }
}
