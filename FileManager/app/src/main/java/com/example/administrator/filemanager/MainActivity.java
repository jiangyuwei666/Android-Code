package com.example.administrator.filemanager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView ;
    Button button ;
    String path ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = ( Button ) findViewById( R.id.button_find );
        textView = ( TextView ) findViewById( R.id.path ) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_GET_CONTENT ) ;//允许用户选择特殊种类的数据，并返回
                intent.setType( "*/*" ) ;//全部类型的文件
                intent.addCategory( Intent.CATEGORY_OPENABLE ) ;
                startActivityForResult(intent , 1 ) ;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData() ;
        if ( "file".equalsIgnoreCase( uri.getScheme() )) {//第三方应用获取
            path = uri.getPath() ;
            textView.setText( path );
            Toast.makeText( this , "succeed" , Toast.LENGTH_SHORT ).show();
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4系统以前
            path = uri.getPath() ;
            textView.setText( path );
            Toast.makeText( this , "succeed2" , Toast.LENGTH_SHORT ).show();
        }
        else {//4.4系统以后
            path = getRealPathFromURI( uri ) ;
            textView.setText( path ) ;
            Toast.makeText( this, "succeed3" ,Toast.LENGTH_SHORT) .show();
        }

    }

    public String getRealPathFromURI (Uri contentUri){
        String res = null ;
        String[] proj = {MediaStore.Images.Media.DATA} ;
        Cursor cursor = getContentResolver().query( contentUri , proj ,null , null,null ) ;
        if ( null != cursor&&cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) ;
            res = cursor.getString(index) ;
        }
        return res ;
    }
}
