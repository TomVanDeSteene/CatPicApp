 package com.tomvandesteene.catpicapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import stanford.androidlib.SimpleActivity;
import stanford.androidlib.xml.XML;

 public class RestActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
    }

    public void chuckNorrisClick(View view){

        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //data has arrived
                        try {
                            JSONObject json = new JSONObject(result);
                            JSONObject value = json.getJSONObject("value");
                            String joke = value.getString("joke");
                            $TV(R.id.output).setText(joke);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    public void catClick(View view){
        GridLayout grid = $(R.id.grid);
        grid.removeAllViews();
        Ion.with(this)
                .load("http://thecatapi.com/api/images/get?format=xml&results_per_page=6")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //data has arrived
                        try {
                            JSONObject json = XML.toJSONObject(result);

                            JSONArray a = json.getJSONObject("response")
                                    .getJSONObject("data")
                                    .getJSONObject("images")
                                    .getJSONArray("image");
                            for (int i = 0; i < a.length(); i++){
                                JSONObject img = a.getJSONObject(i);
                                String url = img.getString("url");
                                log(url);
                                loadImage(url);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

    }

    public void loadImage(String url){

        ImageView imgView = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imgView.setLayoutParams(params);
        GridLayout grid = $(R.id.grid);
        grid.addView(imgView);
        Picasso.with(this)
                .load(url)
                .resize(300, 300)
                .into(imgView);

    }
}
