package com.ramogi.xboxme;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ROchola on 2/7/2015.
 */
public class ImagePlusDownload extends AsyncTask<Void, Void, ArrayList<Drawable>> {

    private Context context1;
    private CallBackImage callBackImage ;
    private Drawable d;
    private String url;
    private ArrayList<String> plusUrls = new ArrayList<String>();
    private ArrayList<Drawable> plusImages = new ArrayList<Drawable>();
    private List<GamersLocation> gamersLocationList;

    public ImagePlusDownload(List<GamersLocation> gamersLocationList, CallBackImage cbi, Context context) {
        this.context1 = context;
        this.callBackImage = cbi;
        this.gamersLocationList = gamersLocationList;

        this.url = url;
        //d = context.getResources().getDrawable(R.drawable.ruth);
    }

    @Override
    protected ArrayList<Drawable> doInBackground(Void... params) {

        plusImages.clear();

        for(GamersLocation gamersLocation : gamersLocationList ){

            url = gamersLocation.getPhotoPath();

            if(url != null){
                //plusUrl.add(urldisplay);
            }
            else {
                url = " else there is no image";
            }

            //Log.v("image empty ", "url is null " + url);

            if(url.trim().isEmpty()){

               Log.v("image download ", "url is null " + url);

                plusImages.add(context1.getResources().getDrawable(R.drawable.turtle));
            }
            else{

                try {
                   Log.v("image download try ", "url being downloaded " + url);
                    InputStream is = new URL(url.trim()).openStream();
                    d = Drawable.createFromStream(is, "src name");
                    is.close();
                    plusImages.add(d);
                    //return d;

                    //return d;
                } catch (Exception e) {
                    Log.v("image download catch ", "url not downloaded " + url);
                    d = context1.getResources().getDrawable(R.drawable.ruth);
                    plusImages.add(d);
                    e.printStackTrace();
                }
            }
        }

        return plusImages;
    }

    @Override
    protected void onPostExecute(ArrayList<Drawable> plusImages) {

        getCallBackImage().querycomplete(plusImages);

    }

    public CallBackImage getCallBackImage() {
        return callBackImage;
    }

    public void setCallBackImage(CallBackImage callBackImage) {
        this.callBackImage = callBackImage;
    }

}
