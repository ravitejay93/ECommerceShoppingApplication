package com.example.ravi.shopping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by ravi on 2/11/2017.
 */

public class categories_view_list extends ArrayAdapter {


    private Context context;
    private ArrayList<String> data;
    private ArrayList<String> images_url;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //super.getView(position, convertView, parent);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.categories_data,null);
        TextView name = (TextView)view.findViewById(R.id.category_name);
        ImageView image = (ImageView)view.findViewById(R.id.category_image);

        name.setText(data.get(position));
        Log.e("Cat",data.get(position));

        get_image img_task = new get_image();

        Bitmap bitmap = null;
        try {
            bitmap = img_task.execute(images_url.get(position)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(bitmap);
        return view;
    }

    public categories_view_list(Context context, ArrayList<String> resource, ArrayList<String> images) {
        super(context,R.layout.fragment_categories,resource);

        this.context = context;
        data = resource;
        images_url = images;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
