package com.app.hakeem.util;

import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewSettings {
	
	public static WebView webView = null;
	
	public static void setWebView(final Activity context, WebView view, View pView) {
		
		webView = view;
		webView.addJavascriptInterface(new JavaScriptInterfaceClass(context, pView),"app");
	    webView.setPadding(0, 0, 0, 0);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setAppCacheEnabled(false); // Earlier - true or false
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setTextZoom(100);
//		webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		webView.getSettings().setSupportMultipleWindows(false);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // Earlier - WebSettings.LOAD_DEFAULT or WebSettings.LOAD_NO_CACHE
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setAllowContentAccess(true);
		webView.getSettings().setAllowFileAccessFromFileURLs(true);
		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		
		//webView.clearCache(true);
		//context.deleteDatabase("webview.db");
		//context.deleteDatabase("webviewCache.db");
		//webView.clearHistory();
	}

}
