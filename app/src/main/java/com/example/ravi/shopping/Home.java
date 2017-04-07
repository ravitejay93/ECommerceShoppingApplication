package com.example.ravi.shopping;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, cart.OnFragmentInteractionListener,address_fragment.OnFragmentInteractionListener, Login.OnFragmentInteractionListener , register.OnFragmentInteractionListener ,update.OnFragmentInteractionListener,categories.OnFragmentInteractionListener,products.OnFragmentInteractionListener,product_details.OnFragmentInteractionListener{

    private TextView side_bar_email;
    private TextView side_user;
    private int user_id = -1;
    public Context context = this;

    private final String CATEGORIES = "CATEGORIES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_id < 0) {
                    Toast.makeText(context, "Please Login", Toast.LENGTH_SHORT).show();
                } else{
                    cart c = cart.newInstance(String.valueOf(user_id));
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_home, c).commit();
                }
                //Snackbar.make(view, "Cart!!!", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        side_bar_email = (TextView) headerView.findViewById(R.id.side_name);
        side_user =(TextView) headerView.findViewById(R.id.side_user);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getFragmentManager().popBackStack(CATEGORIES, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(user_id < 0){
            Toast.makeText(this,"Please Login",Toast.LENGTH_SHORT).show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account) {
            Login login = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home,login).commit();
            return true;
        }
        if (id == R.id.action_update) {

            update u = update.newInstance(String.valueOf(user_id));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, u).commit();

            return true;
        }
        if (id == R.id.action_signout) {
            user_id = -1;
            side_bar_email.setText("E-mail");
            side_user.setText("User");
            categories c = new categories();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home,c).commit();

            return true;
        }

        if (id == R.id.action_address) {

            address_fragment fragment = address_fragment.newInstance(String.valueOf(user_id));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home,fragment).commit();

            return true;
        }

        if(id == R.id.action_cart){

            cart c = cart.newInstance(String.valueOf(user_id));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home,c).commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            categories c = new categories();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home,c,CATEGORIES).commit();

        } else if (id == R.id.nav_deals) {

        } else if (id == R.id.nav_account) {
            Login login = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home,login).commit();

        } else if (id == R.id.nav_orders) {
            if(user_id < 0){
                Toast.makeText(this,"Please Login",Toast.LENGTH_SHORT).show();
            }
            else {
                orders o = orders.newInstance(String.valueOf(user_id));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_home, o).commit();
            }

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_update) {
            if(user_id < 0){
                Toast.makeText(this,"Please Login",Toast.LENGTH_SHORT).show();
            }
            else {
                update u = update.newInstance(String.valueOf(user_id));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_home, u).commit();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int user_id,String email, String name) {
        if(user_id == 0) {
            register r = new register();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, r).commit();
        }
        else{
            //handle user_id
            this.user_id = user_id;
            Log.e("Message",email);
            side_bar_email.setText(email);
            side_user.setText(name);

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(int user_id) {

        this.user_id = user_id;

    }

    @Override
    public void onFragmentInteraction(String type,String id) {
        if(type == "category") {
            products p = products.newInstance(id);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, p).addToBackStack(CATEGORIES).commit();
        }
        else if(type == "product"){
            product_details p =  product_details.newInstance(id);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, p).addToBackStack(CATEGORIES).commit();
        }

    }

    public int get_user(){
        return user_id;
    }
}
