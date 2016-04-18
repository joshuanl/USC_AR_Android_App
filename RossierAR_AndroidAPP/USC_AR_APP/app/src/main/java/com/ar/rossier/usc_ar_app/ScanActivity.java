package com.ar.rossier.usc_ar_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;

import com.firebase.client.Firebase;
import com.moodstocks.android.AutoScannerSession;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.Scanner;

import java.io.UnsupportedEncodingException;

public class ScanActivity extends Activity implements AutoScannerSession.Listener {

  private static final int TYPES = Result.Type.IMAGE | Result.Type.QRCODE | Result.Type.EAN13;

  private AutoScannerSession session = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
    Firebase.setAndroidContext(this);
    SurfaceView preview = (SurfaceView)findViewById(R.id.preview);

    try {
      session = new AutoScannerSession(this, Scanner.get(), this, preview);
      session.setResultTypes(TYPES);
    } catch (MoodstocksError e) {
      e.printStackTrace();
    }
  }
  @Override
  protected void onResume() {
    super.onResume();
    session.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    session.stop();
  }

  @Override
  public void onCameraOpenFailed(Exception e) {
    // You should inform the user if this occurs!
  }

  @Override
  public void onWarning(String debugMessage) {
    // Useful for debugging!
  }

  @Override
  public void onResult(Result result) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setCancelable(false);
      builder.setTitle(result.getType() == Result.Type.IMAGE ? "Image:" : "Barcode:");
      String text = result.getValue();
      byte[] data = Base64.decode(result.getValue(), Base64.DEFAULT);

      text = "null";
      try{
          text = new String(data, "UTF-8");
      }catch (UnsupportedEncodingException e){
          Log.d("Decode", "could not decode msg");
      }
      builder.setMessage("Go to URL?\n" + text);
      final String URL = text;    //url needs to be declared final for Uri.parse() method

      builder.setPositiveButton("Open", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              Uri uri = Uri.parse(URL); // missing 'http://' will cause crashed
              Intent intent = new Intent(Intent.ACTION_VIEW, uri);
              startActivity(intent);
          }
      });

      builder.setNegativeButton("No", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              session.resume();
          }
      });

      builder.show();
  }

}
