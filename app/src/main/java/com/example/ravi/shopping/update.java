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
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link update.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link update#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private mysql_task mysqlTask;
    private mysql_task mysqlTask_info;
    private mysql_task mysqlTask_pwd;
    private mysql_task mysqlTask_dlt;
    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText phone;

    private String current_pwd;
    private EditText current_password;
    private EditText new_password;
    private EditText confirm_new_password;

    private OnFragmentInteractionListener mListener;

    public update() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static update newInstance(String param1) {
        update fragment = new update();
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
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        first_name = (EditText)view.findViewById(R.id.update_firstname);
        last_name = (EditText) view.findViewById(R.id.update_lastname);
        phone = (EditText) view.findViewById(R.id.update_phone);
        email = (EditText) view.findViewById(R.id.update_email);

        current_password = (EditText) view.findViewById(R.id.update_current_password);
        new_password =(EditText)view.findViewById(R.id.update_new_password);
        confirm_new_password = (EditText)view.findViewById(R.id.update_confirm_password);

        mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {
                first_name.setText(mysqlTask.parse(result,"first_name").get(0), TextView.BufferType.EDITABLE);
                last_name.setText(mysqlTask.parse(result,"last_name").get(0), TextView.BufferType.EDITABLE);
                phone.setText(mysqlTask.parse(result,"phone").get(0), TextView.BufferType.EDITABLE);
                email.setText(mysqlTask.parse(result,"email").get(0), TextView.BufferType.EDITABLE);

                current_pwd = mysqlTask.parse(result,"password").get(0);
            }
        };
        mysqlTask.execute("Update_query",mParam1);

        Button update_info = (Button)view.findViewById(R.id.update_customer_info);
        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mysqlTask_info = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                mysqlTask_info.execute("update_info",mParam1,first_name.getText().toString(),last_name.getText().toString(),phone.getText().toString(),email.getText().toString());
            }
        });

        Button password = (Button)view.findViewById(R.id.update_password_info);

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_password.getText().toString().matches("") || new_password.getText().toString().matches("") || confirm_new_password.getText().toString().matches("")){
                    Toast.makeText(getContext(), "You did not enter all fields", Toast.LENGTH_SHORT).show();
                }
                else if(current_password.getText().toString().matches(current_pwd) ){
                    if(new_password.getText().toString().matches(confirm_new_password.getText().toString())){
                        //update password
                        mysqlTask_pwd = new mysql_task(getContext()) {
                            @Override
                            public void onResponseReceived(String result) {

                            }
                        };
                        mysqlTask_pwd.execute("update_pwd",mParam1,new_password.getText().toString());
                    }
                    else{
                        Toast.makeText(getContext(), "new password and confirm do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Password wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button address_add = (Button) view.findViewById(R.id.update_address_info);
        address_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button delete_user = (Button) view.findViewById(R.id.update_delete_user);

        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysql_task mysqlTask1 = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                mysqlTask1.execute("Delete_user",mParam1);
                onButtonPressed(-1);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int user_id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(user_id);
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
        void onFragmentInteraction(int user_id);
    }
}
