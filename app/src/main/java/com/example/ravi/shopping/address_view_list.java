package com.example.ravi.shopping;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by ravi on 3/14/2017.
 */

public class address_view_list extends ArrayAdapter {

    private Context context;
    private ArrayList<String> data;
    private ArrayList<String> aid;
    private ArrayList<String> zip;
    private String uid;

    public mysql_task mysqlTask;


    public address_view_list(Context context, ArrayList<String> resource , ArrayList<String> aid,ArrayList<String> zip,String uid) {

        super(context, R.layout.fragment_address_fragment,resource);
        this.context = context;
        data = resource;
        this.aid = aid;
        this.zip = zip;
        this.uid = uid;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.address_list,null);

        TextView textView = (TextView)view.findViewById(R.id.address_value);
        TextView z = (TextView)view.findViewById(R.id.address_zip);
        Button button = (Button) view.findViewById(R.id.delete_address);

        textView.setText(data.get(position));
        z.setText(zip.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysqlTask = new mysql_task(context) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                String result = null;
                try {
                    result = mysqlTask.execute("delete_address",aid.get(position),uid).get();
                    mysqlTask.getMessage(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
}
