package www.ittepic.edu.servicios_web;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListarAlumnos extends AppCompatActivity{

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_alumnos);
        listView = (ListView)findViewById(R.id.listview);

        List<String> names = new ArrayList<String>();

    }
}
