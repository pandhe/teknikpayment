package com.pontianak.teknikpayment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.pontianak.teknikpayment.R;

import java.net.URISyntaxException;

public class WebTransaksiActivity extends KuberlayarDilautan {
    WebView myWebView;
    ProgressBar pg;
    //MyConfig myConfig=new MyConfig();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_transaksi);
        CookieManager cookieManager = CookieManager.getInstance();

        myWebView =findViewById(R.id.webview);
        pg=findViewById(R.id.progressBar);
        WebSettings webSettings = myWebView.getSettings();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(myWebView,true);
        }
        else { cookieManager.setAcceptCookie(true);
        }
        webSettings.setJavaScriptEnabled(true);
        WebViewClient wvc=new WebViewClient(){
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                setResult(RESULT_OK);
                //finish();
                // lompatkeberanda();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String link1 = "https://vtmap-uploads.s3.amazonaws.com/merchant_logo/G915423405/thumb_retina_thumb_logo_ump.png";
                String link2 = "https://www.untan.ac.id/wp-content/uploads/2019/04/logonew-1.png";
                myWebView.loadUrl("javascript:(function(){document.body.innerHTML = document.body.innerHTML.replace('" + link1+"', '" + link2+"')})()");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("gojek://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);


                        /*Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                        if (intent != null) {
                            view.stopLoading();

                            PackageManager packageManager = WebTransaksiActivity.this.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (info != null) {
                                WebTransaksiActivity.this.startActivity(intent);
                            } else {
                                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                                view.loadUrl(fallbackUrl);
                                newwarningdialog("Tidak dapat mendeteksi aplikasi yang dapat melanjutkan transaksi ini, anda mungkin harus memasang aplikasi payment yang anda pilih");

                                // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                            }


                        }*/
                    } catch (Exception e) {

                        Log.e("ez", "Can't resolve intent://", e);

                    }
                }
                return  true;
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("ez", "monyet");

                if (request.getUrl().toString().startsWith("gojek://")) {
                    try {

                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                       // intent.setData(Uri.parse(url));
                       // startActivity(intent);

                        Intent intent = Intent.parseUri(request.getUrl().toString(), Intent.URI_INTENT_SCHEME);

                        if (intent != null) {
                            view.stopLoading();

                            PackageManager packageManager = WebTransaksiActivity.this.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (info != null) {
                                WebTransaksiActivity.this.startActivity(intent);
                            } else {
                                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                                view.loadUrl(fallbackUrl);
                                newwarningdialog("Tidak dapat mendeteksi aplikasi yang dapat melanjutkan transaksi ini, anda mungkin harus memasang aplikasi payment yang anda pilih");

                                // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                            }


                        }
                    } catch (URISyntaxException e) {

                        Log.e("ez", "Can't resolve intent://", e);

                    }
                }



                return super.shouldOverrideUrlLoading(view, request);
            }
        };

        myWebView.setWebViewClient(wvc);
        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i("ez",String.valueOf(newProgress));


                pg.setProgress(newProgress);
                if(newProgress==100){
                    pg.setVisibility(View.GONE);
                }
                else{
                    pg.setVisibility(View.VISIBLE);
                }
            }




            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i("ez title web",title);
            }


        });

        Intent intent=getIntent();


        myWebView.loadUrl(intent.getStringExtra("url"));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        else{
            myWebView.destroy();
            finish();
        }
       //finish();
        return super.onKeyDown(keyCode, event);
    }
}

