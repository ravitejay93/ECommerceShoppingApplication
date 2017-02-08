package com.example.ravi.shopping;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link register.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private mysql_task mysqlTask;

    private OnFragmentInteractionListener mListener;

    public register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment register.
     */
    // TODO: Rename and change types and number of parameters
    public static register newInstance(String param1, String param2) {
        register fragment = new register();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        final EditText user_name = (EditText) view.findViewById(R.id.new_username);
        final EditText password = (EditText) view.findViewById(R.id.new_password);
        final EditText first_name = (EditText) view.findViewById(R.id.new_firstname);
        final EditText last_name = (EditText) view.findViewById(R.id.new_lastname);
        final EditText address = (EditText) view.findViewById(R.id.new_address);
        final EditText phone = (EditText) view.findViewById(R.id.new_phone);
        final EditText email = (EditText) view.findViewById(R.id.new_email);
        final EditText zip = (EditText)view.findViewById(R.id.new_zip);

        Button submit = (Button)view.findViewById(R.id.submit_form);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mysqlTask = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {
                        if(result == "None"){
                            Toast.makeText(getContext(),"Fields are missing",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(),"user created please Login",Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                mysqlTask.execute("Register",user_name.getText().toString(),password.getText().toString(),first_name.getText().toString(),last_name.getText().toString(),address.getText().toString(),phone.getText().toString(),email.getText().toString(),zip.getText().toString());
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
        void onFragmentInteraction(Uri uri);
    }
}
