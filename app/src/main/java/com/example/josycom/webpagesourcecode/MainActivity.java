package com.example.josycom.webpagesourcecode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{
    private EditText mUrlInput;
    private TextView mCodeTextView;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUrlInput = findViewById(R.id.urlInput);
        mCodeTextView = findViewById(R.id.codeTextView);
        mSpinner = findViewById(R.id.spinner);
        if (getSupportLoaderManager().getLoader(0) != null){
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void getSource(View view) {
        // Get the search string from the input field.
        String spinnerText = mSpinner.getSelectedItem().toString();
        String editTextString = mUrlInput.getText().toString();
        String queryString = spinnerText + editTextString;
        String partialDisplayText = getString(R.string.loading) + queryString;
        //Hide the keyboard when the user taps the button
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        //Check the network connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null){
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0){
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            mCodeTextView.setText(partialDisplayText);
        } else {
            if (editTextString.length() == 0) {
                mCodeTextView.setText(getString(R.string.no_search_term));
            } else {
                mCodeTextView.setText(getString(R.string.no_network));
            }
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null){
            queryString = args.getString("queryString");
        }
        return new WebpageLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        String code = data.toString();
        mCodeTextView.setText(code);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
