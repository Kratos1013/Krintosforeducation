package com.krintos.krintos.activity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Request;
import com.krintos.krintos.app.AppConfig;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.loginandregistration.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.SEARCH_SERVICE;

public class Mathematics extends Fragment implements AdapterView.OnItemSelectedListener {
    public String[] Book = {"B.P.DEMIDOVICH"};

    public     String[] Type = {"Quiz","Exam"};
    public String[] Year = {"1","2","3","4"};
    public String[] For = {"Bachelor","Master"};
    public String[] Semester = {"1","2"};

    private static final int FILE_SELECT_CODE = 0;
    TabHost tabHost;
    private ActionBar mActionBar;
    ProgressDialog progress;
    public static Handler handler;
    public static ProgressBar downprog;
    private TextView dem_book_name ,downloaded;
    private AppCompatImageView dem_download, dem_book_open;
    private FloatingActionButton user_upload;
    public Mathematics() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff00FFED));
        // Inflate the layout for this fragment
        getActivity().setTitle("Mathematics");
        View rootView = inflater.inflate(R.layout.fragment_math, container, false);
        TabHost host = (TabHost) rootView.findViewById(R.id.tabHost);
        host.setup();
        // Progress dialog
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Uploading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("LIBRARY");
        spec.setContent(R.id.LIBRARY);
        spec.setIndicator("LIBRARY");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("SOLUTIONS");
        spec.setContent(R.id.SOLUTIONS);
        spec.setIndicator("SOLUTIONS");
        host.addTab(spec);
        //Tab 3
        spec = host.newTabSpec("INDEX");
        spec.setContent(R.id.INDEX);
        spec.setIndicator("INDEX");
        host.addTab(spec);
        handler = new Handler();
        downprog = (ProgressBar) rootView.findViewById(R.id.downloadprogressBar);
        downprog.setMax(100);
        user_upload = (FloatingActionButton) rootView.findViewById(R.id.user_upload);
        dem_book_name = (TextView) rootView.findViewById(R.id.dem_book_name);
        dem_book_name.setText("B.P.Demidovich");
        downloaded = (TextView) rootView.findViewById(R.id.downloaded);
        dem_download = (AppCompatImageView) rootView.findViewById(R.id.dem_book_download);
        dem_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download(view);
            }
        });
        dem_book_open = (AppCompatImageView) rootView.findViewById(R.id.dem_book_open);
        dem_book_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "demidovich.pdf";
                open_dem_book(filename);
            }
        });
        user_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilePickerActivity.class);

                startActivityForResult(intent, 10);
            }
        });
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File file = new File(extStorageDirectory, "/krintos/books/demidovich.pdf");
        if (!file.exists()) {
            dem_download.setVisibility(View.VISIBLE);

        } else {
            dem_book_open.setVisibility(View.VISIBLE);
            downloaded.setText("Downloaded");
            downloaded.setVisibility(View.VISIBLE);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////// SOLUTIONS ////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.book_spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Book);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Spinner element
        Spinner spinnerType = (Spinner) rootView.findViewById(R.id.spinner_type);
        // Spinner click listener
        spinnerType.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdaptertype = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Type);

        // Drop down layout style - list view with radio button
        dataAdaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerType.setAdapter(dataAdaptertype);

        // Spinner element
        Spinner spinnerYear = (Spinner) rootView.findViewById(R.id.spinner_year);
        // Spinner click listener
        spinnerYear.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterYear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Year);

        // Drop down layout style - list view with radio button
        dataAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerYear.setAdapter(dataAdapterYear);

        // Spinner element
        Spinner spinnerSemester = (Spinner) rootView.findViewById(R.id.spinner_semester);
        // Spinner click listener
        spinnerSemester.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdaptersemester = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Semester);

        // Drop down layout style - list view with radio button
        dataAdaptersemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSemester.setAdapter(dataAdaptersemester);

        // Spinner element
        Spinner spinnerFor = (Spinner) rootView.findViewById(R.id.spinner_for);
        // Spinner click listener
        spinnerFor.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterFor= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, For);

        // Drop down layout style - list view with radio button
        dataAdapterFor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerFor.setAdapter(dataAdapterFor);
        return rootView;
    }


    public void download(View v) {
        new DownloadFile().execute("http://krintos.000webhostapp.com/books/mathematics/demidovich.pdf", "demidovich.pdf");
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "/Krintos/books");

            if (!folder.exists()) {
                folder.mkdirs();
            }
            File pdfFile = new File(folder, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }
    public void open_dem_book(String filename) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Krintos/books", filename);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent1 = Intent.createChooser(intent, "Open With");
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK){
            showDialog();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type = getMimeType(f.getPath());
                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);
                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type", content_type)
                            .addFormDataPart("Uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                            .build();
                    Request request = new Request.Builder()
                            .url(AppConfig.URL_MATH_UPLOAD_FILE)
                            .post(request_body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            throw new IOException("Error :" + response);
                        }
                        hideDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
    private String getMimeType(String path) {
    String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    private void showDialog() {
        if (!progress.isShowing())
            progress.show();
    }
    private void hideDialog() {
        if (progress.isShowing())
            progress.dismiss();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////  SOLUTIONS  //////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinnerbook = (Spinner)parent;
        Spinner spinnertype = (Spinner)parent;
        Spinner spinneryear = (Spinner)parent;
        Spinner spinnerfor = (Spinner)parent;
        Spinner spinnersemester = (Spinner)parent;

        if(spinnerbook.getId() == R.id.book_spinner)
        {
            Toast.makeText(getActivity(), Book[position] + " Selected",Toast.LENGTH_SHORT).show();
        }
        if(spinnertype.getId() == R.id.spinner_type)
        {
            Toast.makeText(getActivity(), "Type :" + Type[position],Toast.LENGTH_SHORT).show();
        }
        if(spinneryear.getId() == R.id.book_spinner)
        {
            Toast.makeText(getActivity(),Year[position] +  " Year" ,Toast.LENGTH_SHORT).show();
        }
        if(spinnerfor.getId() == R.id.spinner_type)
        {
            Toast.makeText(getActivity(), "For " + For[position],Toast.LENGTH_SHORT).show();
        }if(spinnersemester.getId() == R.id.spinner_type)
        {
            Toast.makeText(getActivity(), Semester[position] + " Semester",Toast.LENGTH_SHORT).show();
        }

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}