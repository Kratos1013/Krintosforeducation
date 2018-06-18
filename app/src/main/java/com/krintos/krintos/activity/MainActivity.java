package com.krintos.krintos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import info.androidhive.loginandregistration.R;
import com.krintos.krintos.helper.SQLiteHandler;
import com.krintos.krintos.helper.SessionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{



    private TextView txtusername;
    private TextView txtphone;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mtoolbar;
    private ImageView profile_pic;
    private SQLiteHandler dtbs;
    private SQLiteHandler db;
    private SessionManager session;
    private ImageView blur_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        txtusername = (TextView) header.findViewById(R.id.username);
        txtphone = (TextView) header.findViewById(R.id.phone_number);
        profile_pic = (ImageView) header.findViewById(R.id.profile_pic);
        blur_image = (ImageView) header.findViewById(R.id.nav_image_blur);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        dtbs = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String username = user.get("username");
        String phone = user.get("phone");
        HashMap<String, byte[]> userphoto = dtbs.getUserPhoto();
       byte[] image=null;

        image = userphoto.get("image");
        if (image == null) {
        }else{
            profile_pic.setImageBitmap(getImage(image));
            //setting the blur image
            Bitmap blurredBitmap = BlurBuilder.blur( getApplicationContext(), getImage(image) );
            blur_image.setImageBitmap(blurredBitmap);
        }


        // Displaying the user details on the screen
        txtusername.setText(username);
        txtphone.setText(phone);
        txtphone.setOnClickListener(new View.OnClickListener()
                                    {@Override
                                    public  void onClick(View v){
                                        onTomyaccount(v);
                                    }}
        );

        txtusername.setOnClickListener(new View.OnClickListener()
                                    {@Override
                                    public  void onClick(View v){
                                        onTomyaccount(v);
                                    }}
        );


        header.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            finish();
            startActivity(getIntent());
        }
        });



        if (true){

            Fragment fragment = null;
            fragment = new Main_fragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        }

        if (!session.isLoggedIn()) {
            logoutUser();
        }



    }
    public void onTomyaccount(View view){
        Fragment fragment = null;
        fragment = new MyAccount();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }
    public void onTomainClicked(View view){
        Fragment fragment = null;
        fragment = new Main_fragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;



        if (id == R.id.nav_account) {

            fragment = new MyAccount();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

            // Handle the camera action
        } else if (id == R.id.nav_prog) {

            fragment = new ProgActivity();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        } else if (id == R.id.nav_math) {
            fragment = new Mathematics();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        } else if (id == R.id.nav_phys) {
            fragment = new Physics();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        } else if (id == R.id.nav_chem) {
            fragment = new Chemistry();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        } else if (id == R.id.nav_bio) {
            fragment = new Biology();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        }  else if (id == R.id.nav_phil) {
            fragment = new Philology();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        } else if (id == R.id.nav_set) {
            fragment = new Setting();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();


        } else if (id == R.id.nav_ab) {
            fragment = new About();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();

        }
        else if (id == R.id.nav_header) {


        }
        else if (id == R.id.nav_log) {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Log Out ?")
                    .setMessage("Are you sure you want to Log Out?")
                    .setPositiveButton("Yes, sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            logoutUser();
                            Toast.makeText(getApplicationContext(),
                                    "You can't pass your exams if you do not Log In back now !", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(),
                                    "Thanks for being loyal ! !", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //logout the user button
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.action_settings) {

            fragment = new Setting();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();



        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
/*    public static Bitmap blur(Context context, Bitmap inputBitmap){
        RenderScript rs = RenderScript.create(context);
        Bitmap blurredBitmap = inputBitmap.copy(Bitmap.Config.ALPHA_8,true) ;
        //allocate memory
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());
        //blur image
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.A_8(rs));
        script.setInput(input);
        script.setRadius(10);
        script.forEach(output);
        output.copyTo(blurredBitmap);
        inputBitmap.recycle();
        return blurredBitmap;
    }*/

    }



