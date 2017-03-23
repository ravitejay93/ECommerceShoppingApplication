package com.example.ravi.shopping;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class orders extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public orders() {
        // Required empty public constructor
    }

    public static orders newInstance(String param1) {
        orders fragment = new orders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        order_tabs orderTabs = new order_tabs(getChildFragmentManager(),mParam1);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.order_pager);
        viewPager.setAdapter(orderTabs);
        return view;
    }

}

class order_tabs extends FragmentPagerAdapter{

    private String user_id = null;

    public order_tabs(FragmentManager fm,String user_id) {
        super(fm);
        this.user_id = user_id;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        String value = null;
        if(position == 0){
            value = "Order";
        }
        else if(position == 1){
            value = "Returns";
        }
        //super.getPageTitle(position)
        return value;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);

    }

    @Override
    public Fragment getItem(int position) {
        return order_list.newInstance(position, user_id);
    }

    @Override
    public int getCount() {

        return 2;
    }
}

class order_list extends Fragment {
    public static final String ARG_OBJECT = "object";
    public static final String ARG_PARAM = "user";

    private int position;
    private String user_id;

    public ArrayList<String> odid = null;
    public ArrayList<String> status = null;

    public order_list_view orderListView;

    //public abstract void onSetListner();

    public order_list(){
    }

    public static order_list newInstance(int param,String param1){
        order_list orderList = new order_list();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, param);
        args.putString(ARG_PARAM, param1);
        orderList.setArguments(args);

        return orderList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_OBJECT);
            user_id = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View view = inflater.inflate( R.layout.fragment_orders_list, container, false);
        final ListView order = (ListView)view.findViewById(R.id.list_order);


        mysql_task mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {

            }
        };
        String result= null;
        try {
            if(position == 0) {
                result = mysqlTask.execute("get_orders", user_id).get();
            }
            else{
                result = mysqlTask.execute("get_returns",user_id).get();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if(position == 0) {
            odid = mysqlTask.parse(result, "order_id");
            status = mysqlTask.parse(result, "status");
        }
        else{
            odid = mysqlTask.parse(result, "return_id");
        }

        orderListView = new order_list_view(getContext(), odid, status,null ,position, false) {
            @Override
            public void OnDeleteClick(int position) {

            }
        };
        order.setAdapter(orderListView);
        final int k = position;
        order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderListView = data(odid.get(position),k);
                orderListView.notifyDataSetChanged();
                order.setAdapter(orderListView);
            }
        });
        return view;
    }
    private order_list_view data(final String mParam1,int position){
        mysql_task mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {

            }
        };
        ArrayList<String> status = null;
        ArrayList<String> no = null;
        ArrayList<String> name = null;
        ArrayList<String> quantity = null;
        String result = null;
        try {
            if(position == 0) {
                result = mysqlTask.execute("get_order_list", mParam1).get();
            }
            else{
                result = mysqlTask.execute("get_return_list", mParam1).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.e("CHECK",mParam1);
        int k = 0;

        if(position == 0) {
            no = mysqlTask.parse(result, "product_id");
            name = mysqlTask.parse(result, "product_name");
            status = mysqlTask.parse(result, "status");
            quantity = mysqlTask.parse(result, "quantity");
        }
        else{
            no = mysqlTask.parse(result,"product_id");
            name = mysqlTask.parse(result, "product_name");
            status = mysqlTask.parse(result,"status");
            quantity = mysqlTask.parse(result, "quantity");
            k=1;
        }

        final ArrayList<String> finalNo = no;
        orderListView = new order_list_view(getContext(), name, status,quantity, k, true) {
            @Override
            public void OnDeleteClick(int position) {
                mysql_task mysqlTask = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                //Log.e("VALUE",finalNo.get(position));
                String result = null;
                try {
                    result = mysqlTask.execute("set_return",mParam1, finalNo.get(position)).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                orderListView.remove(orderListView.getItem(position));
                orderListView.notifyDataSetChanged();
            }
        };
        return orderListView;
    }
}

abstract class order_list_view extends ArrayAdapter{

    private ArrayList<String> order_no;
    private ArrayList<String> status;
    private ArrayList<String> quantity;
    private Context context;
    private int position;
    boolean list;

    public abstract void OnDeleteClick(int position);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_list_data,null);

        LinearLayout l = (LinearLayout)view.findViewById(R.id.layout_order);
        LinearLayout l2 = (LinearLayout)view.findViewById(R.id.layout_order_2);

        TextView order_field = (TextView)view.findViewById(R.id.order_field);
        TextView order = (TextView)view.findViewById(R.id.order_number);
        TextView st = (TextView)view.findViewById(R.id.order_status);
        TextView order_quantity_text = (TextView)view.findViewById(R.id.order_quantity_text);
        TextView order_quantity = (TextView)view.findViewById(R.id.order_quantity);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progress_order);

        final Button re = (Button)view.findViewById(R.id.order_return);


        if(!list){
            l.removeView(re);
            l2.removeView(order_quantity);
            l2.removeView(order_quantity_text);
        }
        else{
            order_field.setText("Product Name:");
            order_quantity.setText(quantity.get(position));
            re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OnDeleteClick(position);
                }
            });
        }
        if(list && this.position == 1){
            l.removeView(re);
        }

        order.setText(order_no.get(position));

        int progress = 0;
        if(this.position == 1){
            TextView title = (TextView)view.findViewById(R.id.order_field);
            title.setText("RETURN NO:");
            if(!list) {
                st.setText("No status");
            }
            else{
                st.setText(status.get(position));
                progress = Integer.valueOf(status.get(position));
            }

        }
        else {
            if(status.get(position).matches("None")){
                progress = 0;
            }
            else {
                progress = Integer.valueOf(status.get(position));
            }
            st.setText(status.get(position));
        }

        if(!list) {
            progressBar.setMax(3);
        }
        else{
            progressBar.setMax(2);
        }


        progressBar.setProgress(progress,true);

        return view;
    }

    public order_list_view(Context context, ArrayList<String> order_no, ArrayList<String> status,ArrayList<String> quantity,int position,boolean list) {
        super(context, R.layout.order_list_data,order_no);
        this.context = context;
        this.order_no = order_no;
        this.status = status;
        this.position = position;
        this.list = list;
        this.quantity = quantity;
    }
}

interface orderListListener{
    void onSwitchFragment(String value);
}
