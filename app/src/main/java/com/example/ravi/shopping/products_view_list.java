package com.example.ravi.shopping;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.R.attr.data;

/**
 * Created by ravi on 2/20/2017.
 */


public class products_view_list extends ArrayAdapter {
    private Context context;
    private ArrayList<String> data;
    private ArrayList<String> price;
    private ArrayList<String> url;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.products_data,null);
        TextView name = (TextView)view.findViewById(R.id.product_name);
        TextView p = (TextView)view.findViewById(R.id.product_price);
        ImageView image = (ImageView)view.findViewById(R.id.product_image);

        name.setText(data.get(position));
        Log.e("Cat",data.get(position));

        p.setText(price.get(position));
        Log.e("Cat",price.get(position));

        get_image img_task = new get_image();

        Bitmap bitmap = null;
        try {
            bitmap = img_task.execute(url.get(position)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(bitmap);
        return view;
    }

    public products_view_list(Context context, ArrayList<String> resource,ArrayList<String> price, ArrayList<String> url) {
        super(context,R.layout.fragment_products, resource);
        this.context = context;
        data = resource;
        this.price = price;
        this.url = url;
    }
}
