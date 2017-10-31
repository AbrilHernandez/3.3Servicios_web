package www.ittepic.edu.servicios_web;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button consultar, consultarid, insertar, borrar, actualizar;
    EditText identificador, nombre, dirección;
    TextView resultado;

    //punto wifi
    String IP = "http://192.168.43.242/datos1";
    //Escuela String IP = "http://172.20.2.228/datos1";
    //home
    //String IP = "http://192.168.1.82/datos1";
    String GET_TODO = IP + "/obtener_alumnos.php";
    String GET_BY_ID = IP + "/obtener_alumno_por_id.php";
    String INSERT = IP + "/insertar_alumno.php";
    String DELETE = IP + "/borrar_alumno.php";
    String UPDATE = IP + "/actualizar_alumno.php";

    WServices hconexion;

    String json_string,id,nom,dire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        consultar = (Button) findViewById(R.id.consultar);
        consultarid = (Button) findViewById(R.id.consultarid);
        insertar = (Button) findViewById(R.id.insertar);
        borrar = (Button) findViewById(R.id.borrar);
        actualizar = (Button) findViewById(R.id.actualizar);

        identificador = (EditText) findViewById(R.id.eidentificador);
        nombre = (EditText) findViewById(R.id.enombre);
        dirección = (EditText) findViewById(R.id.edireccion);

        resultado = (TextView) findViewById(R.id.tresultado);

        consultar.setOnClickListener(this);
        consultarid.setOnClickListener(this);
        insertar.setOnClickListener(this);
        borrar.setOnClickListener(this);
        actualizar.setOnClickListener(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.consultar:
                hconexion = new  WServices();
                hconexion.execute(GET_TODO, "1");
                Toast.makeText(getApplicationContext(), "CONSULTAR", Toast.LENGTH_SHORT).show(); break;
            case R.id.consultarid:
                id = identificador.getText().toString();
                if(id.isEmpty()){ Toast.makeText(getApplicationContext(), "INGRESE EL ID", Toast.LENGTH_SHORT).show(); return;}
                hconexion = new WServices();
                hconexion.execute(GET_BY_ID, "2");
                Toast.makeText(getApplicationContext(), "CONSULTAR ID", Toast.LENGTH_SHORT).show(); break;
            case R.id.insertar:
                nom = nombre.getText().toString();
                dire = dirección.getText().toString();
                if(nom.isEmpty() || dire.isEmpty()){Toast.makeText(getApplicationContext(), "POR FAVOR LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show(); return;}
                hconexion = new  WServices();
                hconexion.execute(INSERT, "3",nom,dire);
                Toast.makeText(getApplicationContext(), "INSERTAR", Toast.LENGTH_SHORT).show(); break;
            case R.id.borrar:
                id = identificador.getText().toString();
                if(id.isEmpty()){ Toast.makeText(getApplicationContext(), "INTRODUZCA EL ID A BORRAR", Toast.LENGTH_SHORT).show(); return;}
                hconexion = new  WServices();
                hconexion.execute(DELETE, "4",id);
                Toast.makeText(getApplicationContext(), "BORRAR", Toast.LENGTH_SHORT).show(); break;
            case R.id.actualizar:
                id = identificador.getText().toString();
                nom = nombre.getText().toString();
                dire = dirección.getText().toString();
                if(id.isEmpty() || nom.isEmpty() || dire.isEmpty()){Toast.makeText(getApplicationContext(), "LLENE EL ID, NOMBRE Y DIRECCION", Toast.LENGTH_SHORT).show(); return;}
                hconexion = new  WServices();
                hconexion.execute(UPDATE, "5",id,nom,dire);
                Toast.makeText(getApplicationContext(), "ACTUALIZAR", Toast.LENGTH_SHORT).show(); break;
        }
    }

    public class WServices extends AsyncTask<String, Void, String> {

        URL url;
        @Override
        protected String doInBackground(String... params) {
            String cadena="";
            //String name="";
            //String direc = "";
            String ruta="";
            if (params[1] == "1") {
                try {
                    url = new URL(GET_TODO);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        String temporal = stringBuilder.toString();
                        JSONObject jsonObj = new JSONObject(temporal);
                        // Getting JSON Array node
                        JSONArray alum = jsonObj.getJSONArray("alumnos");

                        // looping through All Contacts
                        for (int i = 0; i < alum.length(); i++) {
                            JSONObject c = alum.getJSONObject(i);
                            cadena += "ID: " + c.getString("idalumno") +" Nombre: " + c.getString("nombre") + " Direccion: " + c.getString("direccion") + "\n";
                        }
                    }
                    else{cadena = ""+respuesta;}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (params[1] == "2") {
                try {
                    url = new URL(GET_BY_ID+"?idalumno="+id);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        //Toast.makeText(getApplicationContext(), "HTTP_OK "+GET_TODO, Toast.LENGTH_SHORT).show();
                        //String abc = json_string;
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        String temporal = stringBuilder.toString();
                        JSONObject jsonObj = new JSONObject(temporal);
                        JSONObject alum = jsonObj.getJSONObject("alumno");
                        cadena += "ID: " + alum.getString("idAlumno") + " Nombre: " + alum.getString("nombre") + "Direccion: " + alum.getString("direccion") + "\n";

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (params[1] == "3") {
                try {
                    url = new URL(INSERT);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    JSONObject json = new JSONObject();
                    json.put("nombre",params[2]);
                    json.put("direccion",params[3]);
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    writer.write(json.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                            // Getting JSON Array node
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        cadena = stringBuilder.toString().trim();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (params[1] == "4") {
                try {
                    url = new URL(DELETE);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    JSONObject json = new JSONObject();
                    json.put("idalumno",params[2]);
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    writer.write(json.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        //Toast.makeText(getApplicationContext(), "HTTP_OK "+GET_TODO, Toast.LENGTH_SHORT).show();
                        //String abc = json_string;
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        cadena = stringBuilder.toString().trim();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (params[1] == "5") {
                try {
                    url = new URL(UPDATE);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestMethod("POST");
                    JSONObject json = new JSONObject();
                    json.put("idalumno",params[2]);
                    json.put("nombre",params[3]);
                    json.put("direccion",params[4]);
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    writer.write(json.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        //Toast.makeText(getApplicationContext(), "HTTP_OK "+GET_TODO, Toast.LENGTH_SHORT).show();
                        //String abc = json_string;
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        cadena = stringBuilder.toString().trim();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return cadena;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultado.setText(s);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
