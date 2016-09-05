package com.example.stratos.posterfun;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.stratos.posterfun.utils.DBContent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class PreLoadActivity extends AppCompatActivity {

    private ProgressBar genLoadProgres;
    private FileDownloader mFD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load);
        genLoadProgres = (ProgressBar) findViewById(R.id.genLoadProgres);

//        File file = new File(DBContent.getDB_PATH());
//        file.delete();


        if (!DBContent.isInitialized()) {
            if (!isOnline()) {
                Toast.makeText(this, "Нет доступа в интернет", Toast.LENGTH_LONG).show();

            } else {
                mFD = new FileDownloader();
                String appDir = getApplicationInfo().dataDir + "/databases";
                mFD.execute(DBContent.getDB_PATH(), DBContent.getUrlDatabase(), appDir);
            }
        } else {
            Intent intent = new Intent(this, ItemListActivity.class);
            startActivity(intent);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getBaseContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        if (mFD != null)
            mFD.cancel(true);
        super.onPause();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.finish();
    }

    class FileDownloader extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(params[1]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File dellFile = new File(params[0]);
                dellFile.delete();
                new File(params[2]).mkdirs();

                OutputStream output = new FileOutputStream(params[0]);
                byte data[] = new byte[256];
                long total = 0;

                while ((count = input.read(data)) != -1) {

                    //Проверяем, актуальна ли еще задача
                    if (isCancelled()) {
                        return null;
                    }
                    total += count;
                    output.write(data, 0, count);

                    //Информирование о закачке.
                    //Передаем число, отражающее процент загрузки файла
                    //После вызова этого метода автоматически будет вызван
                    //onProgressUpdate в главном потоке
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                }
                output.flush();
                output.close();
                input.close();


            } catch (Exception e) {
                Log.d("Stratos", e.getMessage());
                Snackbar.make(genLoadProgres, "Не удалось загрузить файл", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            genLoadProgres.setProgress(progress[0]);
        }


        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Snackbar.make(genLoadProgres.getRootView(), "Не удалось запустить приложение", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(genLoadProgres.getContext(), ItemListActivity.class);
            startActivity(intent);
        }

    }
}
