package com.krintos.krintos.activity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import com.krintos.krintos.app.AppConfig;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.io.File;
import java.io.IOException;
import info.androidhive.loginandregistration.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Physics extends Fragment {
    private static final int FILE_SELECT_CODE = 0;
    TabHost tabHost;
    private ActionBar mActionBar;
    ProgressDialog progress;
    public static Handler handler;
    public static ProgressBar downprog;
    private TextView dem_book_name ,downloaded,dem_book_name1 ,downloaded1,dem_book_name2 ,downloaded2,dem_book_name3,downloaded3;
    private AppCompatImageView dem_download, dem_book_open,dem_download1, dem_book_open1,dem_download2, dem_book_open2,dem_download3, dem_book_open3;
    private FloatingActionButton user_upload;

    public Physics() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Physics");
        View rootView = inflater.inflate(R.layout.fragment_physics, container, false);
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
        downloaded = (TextView) rootView.findViewById(R.id.downloaded);
        dem_download = (AppCompatImageView) rootView.findViewById(R.id.dem_book_download);
        dem_book_open = (AppCompatImageView) rootView.findViewById(R.id.dem_book_open);
        dem_book_name1 = (TextView) rootView.findViewById(R.id.dem_book_name1);
        downloaded1 = (TextView) rootView.findViewById(R.id.downloaded1);
        dem_download1 = (AppCompatImageView) rootView.findViewById(R.id.dem_book_download1);
        dem_book_open1 = (AppCompatImageView) rootView.findViewById(R.id.dem_book_open1);
        dem_book_name2 = (TextView) rootView.findViewById(R.id.dem_book_name2);
        downloaded2 = (TextView) rootView.findViewById(R.id.downloaded2);
        dem_download2 = (AppCompatImageView) rootView.findViewById(R.id.dem_book_download2);
        dem_book_open2 = (AppCompatImageView) rootView.findViewById(R.id.dem_book_open2);
        dem_book_name3 = (TextView) rootView.findViewById(R.id.dem_book_name3);
        downloaded3 = (TextView) rootView.findViewById(R.id.downloaded3);
        dem_download3 = (AppCompatImageView) rootView.findViewById(R.id.dem_book_download3);
        dem_book_open3 = (AppCompatImageView) rootView.findViewById(R.id.dem_book_open3);
        dem_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download(view);
            }
        });
        dem_book_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "irodovobsheyfizike.pdf";
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
        File file = new File(extStorageDirectory, "/krintos/books/irodovobsheyfizike.pdf");
        if (!file.exists()) {
            dem_download.setVisibility(View.VISIBLE);

        } else {
            dem_book_open.setVisibility(View.VISIBLE);
            downloaded.setText("Downloaded");
            downloaded.setVisibility(View.VISIBLE);
        }
        File file1 = new File(extStorageDirectory, "/krintos/books/irodovelectro.pdf");
        if (!file1.exists()) {
            dem_download1.setVisibility(View.VISIBLE);

        } else {
            dem_book_open1.setVisibility(View.VISIBLE);
            downloaded1.setText("Downloaded");
            downloaded1.setVisibility(View.VISIBLE);
        }
        File file2 = new File(extStorageDirectory, "/krintos/books/irodovmech.pdf");
        if (!file2.exists()) {
            dem_download2.setVisibility(View.VISIBLE);

        } else {
            dem_book_open2.setVisibility(View.VISIBLE);
            downloaded2.setText("Downloaded");
            downloaded2.setVisibility(View.VISIBLE);
        }
        File file3 = new File(extStorageDirectory, "/krintos/books/irodovquantum.pdf");
        if (!file3.exists()) {
            dem_download3.setVisibility(View.VISIBLE);

        } else {
            dem_book_open3.setVisibility(View.VISIBLE);
            downloaded3.setText("Downloaded");
            downloaded3.setVisibility(View.VISIBLE);
        }
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
}
