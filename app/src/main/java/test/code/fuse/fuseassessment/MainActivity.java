package test.code.fuse.fuseassessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import FuseApi.CompanyNameContext;
import FuseApi.LogoContext;
import Models.Company;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText etCompanyName;
    private ImageView ivLogo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            InitControls();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void InitControls()throws IOException{
        etCompanyName = (EditText) findViewById(R.id.etCompanyName);
        etCompanyName.setSingleLine(true);
        etCompanyName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCompanyName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(v.getText().length() <= 0){
                        ToastMessage(getApplicationContext(), "Please enter a company name");
                        return false;
                    }
                    new SearchCompanyTask().execute(v.getText().toString());
                    HideKeyboard(v);
                    return false;
                }
                return true;
            }
        });

        ivLogo = (ImageView) findViewById(R.id.ivLogo);
    }

    private class SearchCompanyTask extends AsyncTask<String, Void, Company> {
        private Exception _exception;

        @Override
        protected Company doInBackground(String... params) {
            try {
                String companyName = params[0];
                RequestFuture<JSONObject> companyFuture = RequestFuture.newFuture();
                CompanyNameContext c = new CompanyNameContext();
                c.GetCompanyInfo(getApplicationContext(), companyName, companyFuture, companyFuture);
                JSONObject response = companyFuture.get(10, TimeUnit.SECONDS);
                return new Gson().fromJson(response.toString(), Company.class);
            } catch (Exception e) {
                _exception = e;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            etCompanyName.setBackgroundColor(Color.WHITE);
            ivLogo.setImageDrawable(null);
            ShowProgressDialog("Searching company name");
        }

        @Override
        protected void onPostExecute(Company company) {
            try {
                if (_exception != null) {
                    etCompanyName.setBackgroundColor(Color.RED);
                    DialogMessage(_exception.getMessage(), "Error");
                    return;
                }
                if(company == null){
                    DialogMessage("Empty response from server", "Error");
                    return;
                }

                etCompanyName.setBackgroundColor(Color.GREEN);
                etCompanyName.setText(company.getName());
                DownloadLogo(company.getLogo());
            } catch (Exception e) {
                etCompanyName.setBackgroundColor(Color.RED);
                DialogMessage(e.getMessage(), "Error");
            } finally {
                CloseProgressDialog();
            }
        }
    }

    private void ShowProgressDialog(String message){
        CloseProgressDialog();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void CloseProgressDialog(){
        if(progressDialog != null) progressDialog.dismiss();
    }

    public void DownloadLogo(String url){
        if(url == null){
            DialogMessage("Logo URL not found", "Error");
            return;
        }
        final LogoContext logoContext = new LogoContext();
        ToastMessage(getApplicationContext(), "Downloading Logo...");
        logoContext.GetLogo(getApplicationContext(), url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ivLogo.setImageBitmap(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if(error.networkResponse.statusCode == 301 && error.networkResponse.headers.containsKey("Location")) {
                    logoContext.GetLogo(getApplicationContext(), error.networkResponse.headers.get("Location"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            try {
                                ivLogo.setImageBitmap(response);
                                ToastMessage(getApplicationContext(), "Done");
                            } catch (Exception e){
                                DialogMessage(e.getMessage(), "Error");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            DialogMessage("Error downloading logo", "Error");
                        }
                    });
                    return;
                }
                DialogMessage("Error downloading logo", "Error");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogMessage("Fuse Assessment - Francois van Niekerk", "About");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static void ToastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void DialogMessage(String message, String header) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setTitle(header)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
