package com.krintos.krintos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krintos.krintos.app.AppConfig;
import com.krintos.krintos.helper.SQLiteHandler;
import com.krintos.krintos.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import info.androidhive.loginandregistration.R;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class edit_profile extends Fragment {
    private static final String TAG = edit_profile.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 1;
    private EditText editusername;
    private ImageView editpphoto;
    private TextView editPhone;
    private SQLiteHandler db;
    private SQLiteHandler dtbs;
    private SessionManager session;
    private ProgressDialog pDialog;
    private Bitmap bitmap;
    private Button uploadimage;
    private TextView taptopupload;
    private FloatingActionButton fab;
    public edit_profile() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        getActivity().setTitle("Edit");

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());
        dtbs = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());
        //
        uploadimage = (Button) rootView.findViewById(R.id.upload_image);
        editusername = (EditText) rootView.findViewById(R.id.username);
        editPhone = (TextView) rootView.findViewById(R.id.phone) ;
        editpphoto = (ImageView) rootView.findViewById(R.id.profile_pic);
        taptopupload = (TextView) rootView.findViewById(R.id.tap_change);
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        String username = user.get("username");
        String phone = user.get("phone");




        // Displaying the user details on the screen
        editusername.setText(username);
        editPhone.setText(phone);


        editpphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecImage();
            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();
            }
        });
        // fetching datas from user photo
        HashMap<String, byte[]> userphoto = dtbs.getUserPhoto();
        byte[] image=null;
        image = userphoto.get("image");
        if (image == null) {
        }else{
            editpphoto.setImageBitmap(getImage(image));
        }
        fab = (FloatingActionButton) rootView.findViewById(R.id.save);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Main_fragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();

                }
        });
        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.cancel);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyAccount();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
            }
        });

        return rootView;

    }
    private void selecImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picure"), PICK_IMAGE_REQUEST);

    }
    private void uploadImage() {
        // Tag used to cancel the request

        pDialog.setMessage("Uploading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,AppConfig.URL_UPDATE, new Response.Listener<String>() {
            public  byte[] getBytes(Bitmap bitmap) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                return stream.toByteArray();
            }
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        String phone = editPhone.getText().toString().trim();
                        byte[] image = getBytes(bitmap);
                        dtbs.updateuserphoto();
                        // Inserting row in users table
                        dtbs.addUserPhoto(phone, image);

                        Toast.makeText(getActivity().getApplicationContext(), "Successfully updated", Toast.LENGTH_LONG).show();

                        fab.setVisibility(View.VISIBLE);
                        uploadimage.setVisibility(View.INVISIBLE);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Updating error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                String phone = editPhone.getText().toString().trim();
                String image = getStringImage(bitmap);

                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("image", image);

                params.put("phone", phone);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //Adding request to the queue
        requestQueue.add(strReq);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                editpphoto.setImageBitmap(bitmap);
                uploadimage.setVisibility(View.VISIBLE);
                taptopupload.setVisibility(View.INVISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public  Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
