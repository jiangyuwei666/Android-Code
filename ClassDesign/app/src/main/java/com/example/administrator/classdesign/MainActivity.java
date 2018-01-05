package com.example.administrator.classdesign;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnChoseFile ;
    Button btnTranscode ;
    Button btnTransback ;
    FloatingActionButton btnCamera ;
    TextView tvTranscode ;
    TextView tvTransback ;
    String transcode ;
    String transback ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = ( FloatingActionButton ) findViewById( R.id.button_camera ) ;
        btnChoseFile = ( ImageButton ) findViewById( R.id.button_chose_file ) ;
        btnTranscode = ( Button ) findViewById( R.id.button_transcode ) ;
        btnTransback = ( Button ) findViewById( R.id.button_transback ) ;
        tvTranscode = ( TextView ) findViewById( R.id.text_view_transcode ) ;
        tvTransback = ( TextView ) findViewById( R.id.text_view_transback ) ;
        btnChoseFile.setOnClickListener( this );
        btnTranscode.setOnClickListener( this );
        btnTransback.setOnClickListener( this );
        btnCamera.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.button_camera :
                Toast.makeText( this  , "sb赵笠志点击了相机" , Toast.LENGTH_SHORT ).show();
//                clickCamera();
                break;
            case R.id.button_chose_file :
                Toast.makeText( this , "sb赵笠志点击了文件" , Toast.LENGTH_SHORT ).show();
//                clickChoseFile();
                break;
            case R.id.button_transcode :
                Toast.makeText( this , "sb赵笠志点击了转码" , Toast.LENGTH_SHORT ).show();
//                tvTranscode.setText( transcode ) ;//transcode赋值为返回的编码
                break;
            case R.id.button_transback :
                Toast.makeText( this , "sb赵笠志点击了恢复转码" , Toast.LENGTH_SHORT ).show();
//                tvTransback.setText( transback ) ;//transback赋值为转回去的字符串
                break;
            default:
        }
    }

    /**
     * 点击相机调用的方法
     */
    public void clickCamera () {}

    /**
     * 点击选择文件的方法
     */
    public void clickChoseFile () {}

}
