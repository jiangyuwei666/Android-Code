package com.example.administrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List < Person > personList = new ArrayList<>( ) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPerson();
        PersonAdapter personAdapter = new PersonAdapter( MainActivity.this , R.layout.person_item , personList ) ;
        ListView listView = ( ListView ) findViewById( R.id.list_view ) ;
        listView.setAdapter( personAdapter ) ;
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick ( AdapterView< ? > parent , View view , int position , long id ) {
                Person person = personList.get( position ) ;
                Toast.makeText( MainActivity.this , "你夺了一下" + person.getName() , Toast.LENGTH_SHORT ).show();
            }
        });

    }

    private void initPerson () {
        for ( int i = 0 ; i < 2 ; i ++ ) {
            Person person1 = new Person( "王天锐" , R.drawable.img_2  , "sb") ;
            personList.add( person1 ) ;
            Person person2 = new Person( "陈智彬" , R.drawable.img_2  , "sb") ;
            personList.add( person2 ) ;
            Person person3 = new Person( "和的亲" , R.drawable.img_2  , "sb" ) ;
            personList.add( person3 ) ;
            Person person4 = new Person( "正好" , R.drawable.img_2  ,  "sb" ) ;
            personList.add( person4 ) ;
            Person person5 = new Person( "着离职" , R.drawable.img_2  , "sb" ) ;
            personList.add( person5 ) ;
            Person person6 = new Person( "蒋昱葳" , R.drawable.img_2  , "sb" ) ;
            personList.add( person6 ) ;
            Person person7 = new Person( "屌大音符" , R.drawable.img_2  , "sb" ) ;
            personList.add( person7 ) ;
            Person person8 = new Person( "奥特曼" , R.drawable.img_2  , "sb" ) ;
            personList.add( person8 ) ;
        }
    }
}
