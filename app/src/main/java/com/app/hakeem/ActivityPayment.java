package com.app.hakeem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;
import com.app.hakeem.util.WebViewSettings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityPayment extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private String urlPayment;
    private Dialog progressDialog;
    private boolean isWebViewCalled;
    private boolean isLoadingFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        urlPayment = getIntent().getStringExtra(C.PAYMENT_URL);


        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        startWebView(webView, urlPayment);
        WebViewSettings.setWebView(this, webView, findViewById(R.id.rlPaymentWindow));

    }


    public void startWebView(WebView webView, String pageUrl) {

        progressDialog = Util.getProgressDialog(this, R.string.loading);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                String wvURL = view.getUrl();
                if (wvURL.contains("confirm")) {
//                    tvTitle.setText(ActivityPayment.this.getResources().getString(R.string.confirm_payment));
                }

                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                try {
                    if (!isLoadingFinished) {

                        progressDialog.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {

                    isLoadingFinished = true;
                    progressDialog.dismiss();

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {

        });

        if (!isLoadingFinished) {
            webView.loadUrl(pageUrl);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showAlert();
    }


    public void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.alert));
        alertDialog.setMessage(getResources().getString(R.string.are_sure_you_want));
        alertDialog.setPositiveButton(getText("Yes"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
//                setResult(C.RESULT_PAYMENT_FAILURE);
                finish();

            }
        });
        alertDialog.setNegativeButton(getText("No"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    SpannableString getText(String s) {
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_text)), 0, s.length(), 0);
        return spannableString;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
