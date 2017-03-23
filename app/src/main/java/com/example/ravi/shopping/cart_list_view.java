package com.example.ravi.shopping;

import android.content.Context;
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
 * Created by ravi on 3/16/2017.
 */

public abstract class cart_list_view extends ArrayAdapter {

    private Context context;
    private ArrayList<String> pid;
    private ArrayList<String> quantity;
    private ArrayList<String> name;
    private String user_id;

    public abstract void OnDeleteClick(int position);

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_data,null);

        TextView n = (TextView)view.findViewById(R.id.cart_product_name);
        TextView q = (TextView)view.findViewById(R.id.cart_quantity);

        n.setText(name.get(position));
        q.setText(quantity.get(position));

        Button d = (Button)view.findViewById(R.id.delete_cart);



        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysql_task mysqlTask = new mysql_task(context) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                String result = null;
                try {
                    result = mysqlTask.execute("cart_delete",pid.get(position),user_id).get();
                    mysqlTask.getMessage(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OnDeleteClick(position);
            }
        });
        return view;
    }

    public cart_list_view(Context context, ArrayList<String> pid, ArrayList<String> quantity,ArrayList<String> name,String user) {
        super(context, R.layout.cart_data, pid);
        this.context = context;
        this.pid = pid;
        this.quantity = quantity;
        this.name = name;
        this.user_id = user;
    }
}
