package com.a5harfliler.webviewwithnotification.Parser;

import com.a5harfliler.webviewwithnotification.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielosborne on 3/27/17.
 */

public class PostParser {

    public static List<Post>parseFeed(String content){

        try {
            JSONArray ar = new JSONArray(content);

            List<Post> postList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Post post = new Post();
                post.setId(obj.getInt("id"));
                post.setDate(obj.getString("date"));
                post.setTitle(obj.getString("title"));


                postList.add(post);
            }



            return postList;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }
}
