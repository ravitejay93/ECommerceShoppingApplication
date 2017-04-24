package com.example.ravi.shopping;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.lang.String.valueOf;

public class cart extends Fragment implements AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    //private String mParam2;

    public int position = 0;
    public int sub_position = 0;
    public double total = 0;
    public double final_total = 0;

    public ArrayList<String> sales_tax;

    private TextView tot;

    cart_list_view list;

    private OnFragmentInteractionListener mListener;

    public cart() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static cart newInstance(String param1) {
        cart fragment = new cart();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        View sub_view = inflater.inflate(R.layout.subscribe_order,container,false);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.sub_layout);
        linearLayout.addView(sub_view);
        Spinner spinner_sub = (Spinner)sub_view.findViewById(R.id.month_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.sub_months,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub.setAdapter(arrayAdapter);

        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ListView listView = (ListView)view.findViewById(R.id.list_cart);


        mysql_task mysqlTask = new mysql_task(getContext()){
            @Override
            public void onResponseReceived(String result) {

            }
        };
        String result = null;
        try {
            result = mysqlTask.execute("cart_retrieval",mParam1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        final ArrayList<String> pid = mysqlTask.parse(result,"cart_product_id");
        ArrayList<String> name = mysqlTask.parse(result,"product_name");
        final ArrayList<String> quantity= mysqlTask.parse(result,"quantity_item");
        ArrayList<String> price = mysqlTask.parse(result,"unitprice");

        list = new cart_list_view(getContext(), pid, quantity, name, mParam1) {
            @Override
            public void OnDeleteClick(int position) {
                list.remove(list.getItem(position));
                list.notifyDataSetChanged();
            }
        };
        listView.setAdapter(list);

        mysqlTask = new mysql_task(getContext()) {
            @Override
            public void onResponseReceived(String result) {

            }
        };
        try {
            result = mysqlTask.execute("address_list",mParam1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<String> address = mysqlTask.parse(result,"address");
        final ArrayList<String> aid = mysqlTask.parse(result,"address_id");
        ArrayList<String> z = mysqlTask.parse(result,"zip");
        sales_tax = mysqlTask.parse(result,"sales_tax");

        Spinner spinner = (Spinner)view.findViewById(R.id.cart_spinner);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, address);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.e("sales tax",sales_tax.get(pos));
                final_total = total*(1.0 + Double.valueOf(sales_tax.get(pos))/100.0);
                tot.setText('$'+ String.valueOf(final_total));
                position = pos;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(!pid.get(0).matches("None")) {
            for (int i = 0; i < price.size(); i++) {
                total += Double.valueOf(quantity.get(i)) * Float.valueOf(price.get(i));
            }
            final_total = total*(1.0 + Double.valueOf(sales_tax.get(position))/100.0);
        }

        tot = (TextView)view.findViewById(R.id.total_price);
        tot.setText('$'+ String.valueOf(final_total));
        Button order = (Button)view.findViewById(R.id.place_order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysql_task mysqlTask = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {
                    }
                };
                try {
                    String result = mysqlTask.execute("place_order",mParam1,aid.get(position), valueOf(total)).get();
                    //mysqlTask.parse(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        Button sub = (Button)sub_view.findViewById(R.id.order_subscribe);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysql_task mysqlTask1 = new mysql_task(getContext()) {
                    @Override
                    public void onResponseReceived(String result) {

                    }
                };
                String result;
                try {
                    result = mysqlTask1.execute("subscribe_order",mParam1,aid.get(position),String.valueOf(2*sub_position)).get();
                    mysqlTask1.getMessage(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Quantity");
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.quantity_alert,(ViewGroup)getView(),false);
                final EditText update_quantity = (EditText)view1.findViewById(R.id.update_quantity);
                update_quantity.setText(valueOf(quantity.get(position)));
                builder.setView(view1);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mysql_task mysqlTask = new mysql_task(getContext()) {
                            @Override
                            public void onResponseReceived(String result) {

                            }
                        };
                        try {
                            String result = mysqlTask.execute("cart_update",pid.get(position),mParam1,update_quantity.getText().toString()).get();
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
                builder.show();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
