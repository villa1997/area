package com.example.area;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ladoUno,ladoDos, baseUno, baseDos;
    RadioButton rectangulo,triangulo;
    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        ladoUno = (EditText) findViewById(R.id.valorLadoUno);
        ladoDos = (EditText) findViewById(R.id.valorLadoDos);
        baseUno = (EditText) findViewById(R.id.baseUno);
        baseDos = (EditText) findViewById(R.id.baseDos);
        rectangulo = (RadioButton) findViewById(R.id.radioAreaRectangulo);
        triangulo = (RadioButton) findViewById(R.id.radioAreaTriangulo);
        resultado = (TextView) findViewById(R.id.resultado);

        ConsultaDatos(null);
        //String valueBefore = preferences.getString("calculos","");
        //resultado.setText("El valor anterior fue: " + valueBefore);

    }

    public void calcularAreas(View view) throws Exception{

        AdminBD adminBD = new AdminBD(this, "BaseData", null, 1);

        SQLiteDatabase sqLiteDatabase = adminBD.getWritableDatabase();

        String tipo = "";
        //SharedPreferences preferencias = getSharedPreferences("datos",Context.MODE_PRIVATE);

       // SharedPreferences.Editor ObjetoEditor = preferencias.edit();
        try
        {
            if (rectangulo.isChecked()){
                int sideOne = Integer.parseInt(ladoUno.getText().toString());
                int sideTwo = Integer.parseInt(ladoDos.getText().toString());
                resultado.setText(String.valueOf(sideOne*sideTwo));
                tipo = "Rectangulo";
               // ObjetoEditor.putString("calculos",resultado.getText().toString());

            }
            if(triangulo.isChecked()){
                float baseOne = Float.parseFloat(baseUno.getText().toString());
                float baseTwo = Float.parseFloat(baseDos.getText().toString());
                resultado.setText(String.valueOf(baseOne*baseTwo/2));
                tipo = "Triangulo";
                //ObjetoEditor.putString("calculos",resultado.getText().toString());
            }

            ContentValues insertar = new ContentValues();
            insertar.put("Calculado",resultado.getText().toString());
            insertar.put("TipoCalculo",tipo);
            sqLiteDatabase.insert("Area", null, insertar);
            sqLiteDatabase.close();
            Toast.makeText(this,"Registro de area almacenado en la base de datos", Toast.LENGTH_SHORT).show();

            //ObjetoEditor.commit();
           // String valueBefore = preferencias.getString("calculos","");
            //resultado.setText("El valor almacenado en memoria fue: " + valueBefore);
            //finish();
        } catch(Exception  ex)
        {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(MainActivity.this);

            // Set the message show for the Alert time
            builder.setMessage(ex.getMessage());

            // Set Alert Title
            builder.setTitle("Alert !");
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        }
    }

    public void ConsultaDatos(View view)
    {
        AdminBD adminBD = new AdminBD(this, "BaseData", null, 1);

        SQLiteDatabase sqLiteDatabase = adminBD.getWritableDatabase();

        Cursor fila = sqLiteDatabase.rawQuery("select Calculado, TipoCalculo from Area order by IdRows desc limit 1", null);

        if(fila.moveToFirst())
        {
            resultado.setText("La ultima area del: "+ fila.getString((1)) + " = " + fila.getString((0)));
        }

    }

    public void ClearValues(View view){
        ladoUno.setText("");
        ladoDos.setText("");
        baseUno.setText("");
        baseDos.setText("");
        resultado.setText("");
    }
}