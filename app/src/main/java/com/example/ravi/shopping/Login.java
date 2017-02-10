package com.example.ravi.shopping;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText user_name = null;
    private EditText password = null;

    private mysql_task mysqlTask;

    private int user_id = -1;
    private String email;

    private OnFragmentInteractionListener mListener;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        user_name = (EditText) view.findViewById(R.id.user_name);
        password = (EditText) view.findViewById(R.id.password);
        final Button login = (Button) view.findViewById(R.id.login);
        Button reg = (Button) view.findViewById(R.id.register_user);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = user_name.getText().toString();
                String p = password.getText().toString();
                mysqlTask = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {
                        String val = mysqlTask.parse(result,"user_id").get(0);
                        if(val != "None") {
                            user_id = Integer.valueOf(val);
                            Toast.makeText(getContext(),"User present",Toast.LENGTH_SHORT).show();
                            email = mysqlTask.parse(result,"email").get(0);
                            //TextView textView = (TextView) view.findViewById(R.id.side_name);
                            //textView.setText(mysqlTask.parse(result,"email").get(0));
                            //Log.e("message",mysqlTask.parse(result,"email").get(0));
                            if(user_id > 0){
                                onButtonPressed(user_id,email,mysqlTask.parse(result,"username").get(0));
                            }
                        }
                        else{
                            //Toast.makeText(getContext(),"Please register/retry",Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                if(u.matches("") || p.matches("")){
                    Toast.makeText(getContext(), "Enter all field(s)", Toast.LENGTH_SHORT).show();
                }
                else {
                    mysqlTask.execute("Login", u, p);
                }

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(0,"","");
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int user_id,String email, String name) {
        if (mListener != null) {
            mListener.onFragmentInteraction(user_id,email,name);
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
        void onFragmentInteraction(int user_id,String email,String name);
    }
}
