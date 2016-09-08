package com.example.zxingdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.karics.library.zxing.android.CaptureActivity;
import com.karics.library.zxing.encode.CodeCreator;

public class MainActivity extends Activity {

	private static final int REQUEST_CODE_SCAN = 0x0000;

	private static final String DECODED_CONTENT_KEY = "codedContent";
	private static final String DECODED_BITMAP_KEY = "codedBitmap";

	TextView qrCoded;
	ImageView qrCodeImage;
	Button creator, scanner;
	EditText qrCodeUrl;

	//权限相关
	private static final int REQUEST_FOR_PERMISSIONS = 1;
	private static String[] PERMISSIONS = {
			Manifest.permission.READ_EXTERNAL_STORAGE
			, Manifest.permission.WRITE_EXTERNAL_STORAGE
			, Manifest.permission.CAMERA
			, Manifest.permission.ACCESS_FINE_LOCATION
			, Manifest.permission.ACCESS_COARSE_LOCATION
			, Manifest.permission.RECORD_AUDIO
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//申请权限
		int permission;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			//读写权限&摄像头权限
			permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if (permission != PackageManager.PERMISSION_GRANTED) {
				// We don't have permission so prompt the user
				this.requestPermissions(PERMISSIONS, REQUEST_FOR_PERMISSIONS);
			}

		}

		qrCoded = (TextView) findViewById(R.id.ECoder_title);
		qrCodeImage = (ImageView) findViewById(R.id.ECoder_image);
		creator = (Button) findViewById(R.id.ECoder_creator);
		scanner = (Button) findViewById(R.id.ECoder_scaning);
		qrCodeUrl = (EditText) findViewById(R.id.ECoder_input);

		creator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String url = qrCodeUrl.getText().toString();
				try {
					Bitmap bitmap = CodeCreator.createQRCode(url);
					qrCodeImage.setImageBitmap(bitmap);
				} catch (WriterException e) {
					e.printStackTrace();
				}

			}
		});

		scanner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SCAN);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {

				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

				qrCoded.setText("解码结果： \n" + content);
				qrCodeImage.setImageBitmap(bitmap);
			}
		}
	}
}
