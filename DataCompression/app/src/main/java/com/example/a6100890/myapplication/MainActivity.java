package com.example.a6100890.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a6100890.myapplication.imageRecognition.AppConfiguration;
import com.example.a6100890.myapplication.imageRecognition.RecognitionUtils;
import com.example.a6100890.myapplication.imageRecognition.bean.OCRCharDataJson;
import com.example.a6100890.myapplication.imageRecognition.constant.Constants;
import com.example.a6100890.myapplication.imageRecognition.utils.HttpUtil;
import com.example.a6100890.myapplication.model.EncodeResult;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CHOOSE_FILE = 4;

    private TextView tvPath;
    private TextView tvEncode;
    private TextView tvDecode;
    private ImageButton btnEncode;
    private ImageButton btnDecode;
    private ImageButton btnChoose;
    private ImageButton btnCamera;
    private Huffman mHuffman;
    private String selectedFilepath;
    private File mOutputImage;
    private Uri mImageUri;
    private StringBuilder letterCodeStr = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request();

        tvPath = (TextView) findViewById(R.id.text_view_path);
        tvEncode = (TextView) findViewById(R.id.text_view_compress);
        tvDecode = (TextView) findViewById(R.id.text_view_decode);
        btnEncode = (ImageButton) findViewById(R.id.button_compress);
        btnDecode = (ImageButton) findViewById(R.id.button_decode);
        btnCamera = (ImageButton) findViewById(R.id.button_camera);
        btnChoose = (ImageButton) findViewById(R.id.button_chose_file);
        tvDecode.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvEncode.setMovementMethod(ScrollingMovementMethod.getInstance());
        StatusBarUtil.setTransparent( this );
        final String decoderPath = getApplicationContext().getCacheDir().getAbsolutePath() + "/" + "decoder.txt";

        mHuffman = new Huffman();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//允许用户选择特殊种类的数据，并返回
                intent.setType("*/*");//全部类型的文件
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CHOOSE_FILE);
            }
        });

        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (selectedFilepath == null) {
                        Toast.makeText(MainActivity.this, "请先选择文件路径", Toast.LENGTH_SHORT).show();
                    } else {
                        EncodeResult encodeResult = mHuffman.encode(selectedFilepath, decoderPath);
                        String encodeStr = encodeResult.getEncode();
                        Map<Character, String> letterCode = encodeResult.getLetterCode();

                        letterCodeStr.delete(0, letterCodeStr.length());
                        for (Character c : letterCode.keySet()) {
                            letterCodeStr.append(c + "\t : " + letterCode.get(c) + "\n");
                        }

                        Log.d(TAG, "onActivityResult: " + encodeStr);
                        tvEncode.setText(encodeStr);
                        tvDecode.setText(letterCodeStr.toString());
                        tvPath.setText(selectedFilepath);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilepath == null) {
                    Toast.makeText(MainActivity.this, "请先选择文件路径", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String encodeFilename = selectedFilepath.substring(0, selectedFilepath.lastIndexOf('/') + 1) + "encodeResult.txt";
                        Log.d(TAG, "onClick btnDecode: encodeFilename: " + encodeFilename);
                        String decodeStr = mHuffman.decode(encodeFilename, decoderPath);
                        Log.d(TAG, "onActivityResult: " + decodeStr);

                        tvEncode.setText(decodeStr);
                        tvDecode.setText(letterCodeStr.toString());
                        tvPath.setText(selectedFilepath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        tvPath.setTextSize(20);

        switch (requestCode) {
            case REQUEST_CHOOSE_FILE:
                selectedFilepath = getFilePath(data);
                tvPath.setText(selectedFilepath);
                break;

            case REQUEST_CAMERA:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                    //更改了代码,实现识别并压缩
                    recognizeChar(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            default:
                Log.d(TAG, "onActivityResult: default");
                break;
        }
    }

    private String getFilePath(Intent data) {
        Uri uri = data.getData();
        String path;
        if ("file".equalsIgnoreCase(uri.getScheme())) {//第三方应用获取
            path = uri.getPath();
            Log.d(TAG, "onActivityResult1: " + path);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {//4.4系统以前
            path = uri.getPath();
            Log.d(TAG, "onActivityResult2: " + path);
        } else {//4.4系统以后
            path = getRealPathFromURI(uri);
            Log.d(TAG, "onActivityResult3: " + path);
        }

        return path;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(index);
        }
        return res;
    }

    private void openCamera() {
        if (mOutputImage == null) {
            mOutputImage = new File(getExternalCacheDir(), "output_image.jpg");
        }
        try {
            if (mOutputImage.exists()) {
                mOutputImage.delete();
            }
            mOutputImage.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            mImageUri = FileProvider.getUriForFile(this, "com.example.a6100890.myapplication.fileprovider", mOutputImage);
        } else {
            mImageUri = Uri.fromFile(mOutputImage);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * 通用文字识别
     */
    private String recognizeChar(Bitmap bitmap) {
        final StringBuilder sb = new StringBuilder();
        final OCRCharDataJson[] dataJson = new OCRCharDataJson[1];
        String base64Img = RecognitionUtils.Bitmap2String(bitmap);
        final String body = " {\"image\":\" " + base64Img + "\", \"configure\":\"{\\\"min_size\\\" : 16,\\\"output_prob\\\" : true }\"}";
        final String getPath = "/api/predict/ocr_general";
//        final Intent cardPublishIntent = new Intent(this, CardPublishActivity.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.getInstance().httpPostBytes(AppConfiguration.ChAR_BASE_URL, getPath, null, null, body.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: recognizeChar: " + e.getMessage(), e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        StringBuilder result = new StringBuilder();

                        if (response.code() != 200) {
                            Log.d("CharOCR", result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF).toString());
                            return;
                        }

                        result.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));

                        String resultJson = result.toString();
                        Log.d(TAG, "CharOCR: onResponse: " + resultJson);
                        dataJson[0] = new Gson().fromJson(resultJson, OCRCharDataJson.class);


                        //更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (OCRCharDataJson.Ret ret : dataJson[0].getRet()) {
                                    String mess = ret.getWord();
                                    Log.d(TAG, "run: " + mess);
                                    sb.append(mess);
                                }

                                //压缩文字
                                Log.d(TAG, "run: 识别结果: " + sb.toString());
                                EncodeResult encodeResult = mHuffman.encode(sb.toString());
                                String encodeStr = encodeResult.getEncode();
                                Log.d(TAG, "onActivityResult: " + encodeStr);

                                StringBuilder letterCodeStr = new StringBuilder();
                                Map<Character, String> letterCode = encodeResult.getLetterCode();
                                for (Character c : letterCode.keySet()) {
                                    letterCodeStr.append(c + "\t : " + letterCode.get(c) + "\n");
                                }
                                tvEncode.setText(encodeStr);
                                tvDecode.setText(letterCodeStr.toString());
                                tvPath.setText(sb.toString());
                            }
                        });
                    }
                });
            }
        }).start();

        return sb.toString();
    }

    /**
     * 权限的动态申请
     */
    public void request() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        /*if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS );
        }*/
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
//                for ( int result : grantResults ) {
//                    if ( result != PackageManager.PERMISSION_GRANTED ) {
//
//                        Toast.makeText(MainActivity.this , "请允许申请必要的权限哟~" , Toast.LENGTH_SHORT ).show();
//                        finish();
//                        return;
//                    }
//                }
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, "onRequestPermissionsResult: " + permissions[i]);
                        Toast.makeText(MainActivity.this, "请允许申请必要的权限哟~", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.finishAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }
}