package com.example.ravi.shopping;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class categories extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private mysql_task mysqlTask;
    private ArrayList<String> name;
    private ArrayList<String> url;
    private ArrayList<String> c_id;
    private categories_view_list list;
    private ListView listView;
    private Context context;

    private OnFragmentInteractionListener mListener;

    public categories() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static categories newInstance(String param1) {
        categories fragment = new categories();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_categories, container, false);
        context = getContext();
        listView = (ListView)view.findViewById(R.id.list_categories);
        mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {

            }
        };
        String result = null;
        try {
            result = mysqlTask.execute("categories_list").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        name = mysqlTask.parse(result,"category_name");
        url = mysqlTask.parse(result,"category_url");
        c_id = mysqlTask.parse(result,"category_id");

        if(name!= null && url != null && c_id != null) {
            list = new categories_view_list(context, name, url);

            listView.setAdapter(list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onButtonPressed(c_id.get(position));
                    Toast.makeText(getContext(), name.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String category) {
        if (mListener != null) {
            mListener.onFragmentInteraction("category",category);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String type,String id);
    }

}
