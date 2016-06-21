package com.kocapplication.pixeleye.kockocapp.main.story;

/**
 * Created by Han_ on 2016-06-21.
 */
public class StoryThread extends Thread {
    private final String postURL = "";

    public StoryThread() {
        super();
    }

    @Override
    public void run() {
        super.run();
    }

    //Gson class to json 예제
//    DBConnect는 이 방식대로 하시면 됩니당!
//    String result = DBConnect.getInstance.dataPost(postURL, data);
//    System.out.println(result);
//    if (result.equals("1")) handler.sendMessage(true);
//    else handler.sendMessage(false);

   //Gson 하나씩 예제
//    JsonParser parser = new JsonParser();
//    JsonObject object = new JsonObject();
//    object.add("id", parser.parse(id));
//    object.add("pw", parser.parse(pw));
//
//    String receive = connect.dataPost(postURL, object.toString());
//
//    try {
//        JsonArray array = parser.parse(receive).getAsJsonArray();
//        object = array.get(0).getAsJsonObject();
//        int index = object.get("index").getAsInt();
//        String shopName = object.get("shopName").getAsString();
//
//        ClientCommon.getInstance().setUSERINDEX(index);
//        ClientCommon.getInstance().setUSERNAME(shopName);
//
//        handler.sendMessage(true);
//    } catch (Exception e) {
//        e.printStackTrace();
//        new AlertDialog("ID랑 PASSWORD가 틀렸어요 ㅠㅅㅜ");
//        handler.sendMessage(false);
//    }
}
