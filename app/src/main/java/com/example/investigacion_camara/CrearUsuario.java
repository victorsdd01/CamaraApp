package com.example.investigacion_camara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import com.example.investigacion_camara.BaseDeDatos.Camara;
import com.example.investigacion_camara.Splash_Activities.SplashSuccsses;
import com.google.android.material.textfield.TextInputEditText;

public class CrearUsuario extends AppCompatActivity {

    TextInputEditText edtx_usuario, edtx_password,edtx_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
        IniciarController();

    }

    private void IniciarController()
    {
        edtx_usuario=(TextInputEditText)findViewById(R.id.crearU_edtx_Usuario);
        edtx_password=(TextInputEditText)findViewById(R.id.crearU_edtx_Password);
        edtx_email=(TextInputEditText)findViewById(R.id.crearU_edtx_Email);
    }


    public void Crear(View view)
     {

         String usuario,password,email;
         usuario=edtx_usuario.getText().toString();
         password=edtx_password.getText().toString();
         email=edtx_email.getText().toString();

         if(!usuario.isEmpty()){
             if(!password.isEmpty()){
                 if(!email.isEmpty()){

                     registrarUsuario();
                 }else{}
             }else{}
         }else{}


     }

     public void registrarUsuario()
      {
          String usuario,password,email;
          usuario=edtx_usuario.getText().toString();
          password=edtx_password.getText().toString();
          email=edtx_email.getText().toString();

          Camara camara = new Camara(getApplicationContext(),"db_camara",null,1);
          if(camara!=null){
              try {
                  SQLiteDatabase sql_dataBase = camara.getWritableDatabase();
                  ContentValues registrar_usuario= new ContentValues();

                  registrar_usuario.put("nombre",usuario);
                  registrar_usuario.put("password",password);
                  registrar_usuario.put("email",email);

                  sql_dataBase.insert("usuarios",null,registrar_usuario);
                  sql_dataBase.close();

                  startActivity(new Intent(getApplicationContext(), SplashSuccsses.class));

              }catch (Exception e){}
          }else{}

      }

}// llave  de la clase...