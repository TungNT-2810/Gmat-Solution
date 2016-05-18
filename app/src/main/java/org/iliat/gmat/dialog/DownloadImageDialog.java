package org.iliat.gmat.dialog;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.iliat.gmat.R;
import org.iliat.gmat.utils.Decompress;

import java.io.File;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by hungtran on 5/4/16.
 */
public class DownloadImageDialog extends DialogFragment implements Runnable{
    private ProgressBar progressBar;
    private Button btnClose;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_download_controller, container);
        getRefercenceView(view);
        startDownload();
        return view;
    }

    public void getRefercenceView(View view){
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar_download);
        progressBar.setMax(100);
        btnClose = (Button)view.findViewById(R.id.btn_close_download);
        btnClose.setEnabled(false);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadImageDialog.this.dismiss();
            }
        });
    }

    public void startDownload(){
        Ion.with(getActivity())
                .load("http://iliat.org/image.zip")
                .progressBar(progressBar)
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {

                        //progressBar.setProgress((int)(downloaded * 1.0f / total)* 100);
                        Log.d("DOwnload", downloaded + "/" + total);
                    }
                })
                .write(new File("/sdcard/image-gmat.zip"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        Thread thread = new Thread(DownloadImageDialog.this);
                        thread.start();
                        enableButton();
                    }
                });

    }

    private void enableButton(){
        btnClose.setEnabled(true);
    }

    @Override
    public void run() {
        Decompress decompress = new Decompress("/sdcard/image-gmat.zip", "/storage/emulated/0/gmat-image/");
        decompress.unzip();
    }
}
