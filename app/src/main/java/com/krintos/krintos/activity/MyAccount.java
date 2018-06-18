package com.krintos.krintos.activity;



import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.krintos.helper.SQLiteHandler;
import com.krintos.krintos.helper.SessionManager;

import java.util.HashMap;

import info.androidhive.loginandregistration.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccount extends Fragment {
    private TextView txtUserName;
    private TextView txtphone;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mtoolbar;
    private Button btnLogout;
    private ImageView profile_pic;
    private Button btnEdit;
    boolean isImageFitToScreen;
    private SQLiteHandler dtbs;
    private SQLiteHandler db;
    private SessionManager session;
    private Bitmap bitmap;
    public static int screenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public MyAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        getActivity().setTitle("My Account");

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_Edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new edit_profile();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();


            }
        });

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());
        dtbs = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());
        txtUserName = (TextView) rootView.findViewById(R.id.username);
        txtphone = (TextView) rootView.findViewById(R.id.phone_number);
        profile_pic =(ImageView) rootView.findViewById(R.id.profile_pic);

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String username = user.get("username");
        String phone = user.get("phone");

        // fetching datas from user photo
        HashMap<String, byte[]> userphoto = dtbs.getUserPhoto();
        byte[] image=null;
        image = userphoto.get("image");
        if (image == null) {
            profile_pic.setVisibility(View.INVISIBLE);
        }else{
            profile_pic.setImageBitmap(getImage(image));
            // Displaying the user details on the screen
            txtUserName.setText(username);
            txtphone.setText(phone);
            int bitmapWidth = getImage(image).getWidth();
            int bitmapHeight = getImage(image).getHeight();
            int screenWidth = screenWidth();
            int screenHeight = screenHeight();

                 /*  profile_pic.setImageBitmap(decodedImage);*/
            // set maximum scroll amount (based on center of image)
            int maxX = (int)((bitmapWidth / 2) - (screenWidth / 2));
            int maxY = (int)((bitmapHeight / 2) - (screenHeight / 2));

            // set scroll limits
            final int maxLeft = (maxX * -1);
            final int maxRight = maxX;
            final int maxTop = (maxY * -1);
            final int maxBottom = maxY;
            // set touchlistener
        profile_pic.setOnTouchListener(new View.OnTouchListener()
        {
            float downX, downY;
            int totalX, totalY;
            int scrollByX, scrollByY;
            public boolean onTouch(View view, MotionEvent event)
            {
                float currentX, currentY;
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        currentX = event.getX();
                        currentY = event.getY();
                        scrollByX = (int)(downX - currentX);
                        scrollByY = (int)(downY - currentY);

                        // scrolling to left side of image (pic moving to the right)
                        if (currentX > downX)
                        {
                            if (totalX == maxLeft)
                            {
                                scrollByX = 0;
                            }
                            if (totalX > maxLeft)
                            {
                                totalX = totalX + scrollByX;
                            }
                            if (totalX < maxLeft)
                            {
                                scrollByX = maxLeft - (totalX - scrollByX);
                                totalX = maxLeft;
                            }
                        }

                        // scrolling to right side of image (pic moving to the left)
                        if (currentX < downX)
                        {
                            if (totalX == maxRight)
                            {
                                scrollByX = 0;
                            }
                            if (totalX < maxRight)
                            {
                                totalX = totalX + scrollByX;
                            }
                            if (totalX > maxRight)
                            {
                                scrollByX = maxRight - (totalX - scrollByX);
                                totalX = maxRight;
                            }
                        }

                        // scrolling to top of image (pic moving to the bottom)
                        if (currentY > downY)
                        {
                            if (totalY == maxTop)
                            {
                                scrollByY = 0;
                            }
                            if (totalY > maxTop)
                            {
                                totalY = totalY + scrollByY;
                            }
                            if (totalY < maxTop)
                            {
                                scrollByY = maxTop - (totalY - scrollByY);
                                totalY = maxTop;
                            }
                        }

                        // scrolling to bottom of image (pic moving to the top)
                        if (currentY < downY)
                        {
                            if (totalY == maxBottom)
                            {
                                scrollByY = 0;
                            }
                            if (totalY < maxBottom)
                            {
                                totalY = totalY + scrollByY;
                            }
                            if (totalY > maxBottom)
                            {
                                scrollByY = maxBottom - (totalY - scrollByY);
                                totalY = maxBottom;
                            }
                        }

                        profile_pic.scrollBy(scrollByX, scrollByY);
                        downX = currentX;
                        downY = currentY;
                        break;

                }

                return true;
            }
        });

        }



        return rootView;
    }


    public  Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
