package com.example.ravi.shopping;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class address_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private ListView listView;

    private ArrayList<String> address;
    private ArrayList<String> z;
    private ArrayList<String> aid;

    private mysql_task mysqlTask;

    protected EditText editText;

    protected EditText zip;

    private address_view_list list;

    private OnFragmentInteractionListener mListener;

    public Context c;

    public address_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static address_fragment newInstance(String param1) {
        address_fragment fragment = new address_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        View view = inflater.inflate(R.layout.fragment_address_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.list_address);

        c = getContext();

        mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {

            }
        };
        String result = null;
        try {
            result = mysqlTask.execute("address_list",mParam1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        address = mysqlTask.parse(result,"address");
        aid = mysqlTask.parse(result,"address_id");
        z = mysqlTask.parse(result,"zip");



        if(address!= null) {
            list = new address_view_list(c,address,aid,z,mParam1);

            listView.setAdapter(list);

        }

        editText = (EditText) view.findViewById(R.id.new_address);
        zip = (EditText)view.findViewById(R.id.add_zip);

        Button button = (Button)view.findViewById(R.id.submit_address);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().matches("")){
                    Toast.makeText(c,"Enter Address",Toast.LENGTH_SHORT).show();
                    return;
                }
                mysqlTask = new mysql_task(c) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                String result = null;
                try {
                    result = mysqlTask.execute("add_address",mParam1,editText.getText().toString(),zip.getText().toString()).get();
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
