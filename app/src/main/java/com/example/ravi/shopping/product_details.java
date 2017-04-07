package com.example.ravi.shopping;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class product_details extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private mysql_task mysqlTask;
    private String url;


    private OnFragmentInteractionListener mListener;

    public product_details() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static product_details newInstance(String param1) {
        product_details fragment = new product_details();
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
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView price = (TextView)view.findViewById(R.id.price);
        TextView description = (TextView)view.findViewById(R.id.description);
        TextView available_q = (TextView)view.findViewById(R.id.product_available);

        final EditText quantity = (EditText)view.findViewById(R.id.product_qunatity);


        ImageView image = (ImageView)view.findViewById(R.id.image);

        mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {

            }
        };
        String result = null;
        try {
            result = mysqlTask.execute("product_details",mParam1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        name.setText(mysqlTask.parse(result,"product_name").get(0));
        url = mysqlTask.parse(result,"image").get(0);
        price.setText(mysqlTask.parse(result,"unitprice").get(0));
        description.setText(mysqlTask.parse(result,"product_description").get(0));
        available_q.setText(mysqlTask.parse(result,"quantity").get(0));

        get_image img_task = new get_image();

        Bitmap bitmap = null;
        try {
            bitmap = img_task.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(bitmap);
        final int user_id = ((Home)getActivity()).get_user();

        Button button = (Button) view.findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = null;
                mysqlTask = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                try {
                    result = mysqlTask.execute("add_cart",mParam1,String.valueOf(user_id),quantity.getText().toString()).get();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
