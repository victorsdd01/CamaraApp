package com.example.investigacion_camara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.investigacion_camara.BaseDeDatos.Camara;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IniciarController();
    }

    private void IniciarController()
     {
         try {
             email=(TextInputEditText)findViewById(R.id.Login_edtx_usuario);
             password=(TextInputEditText)findViewById(R.id.Login_edtx_password);

         }catch (Exception e ){Toast.makeText(getApplicationContext(),"Ha ocurrido una excepcion al cargar los controladores",Toast.LENGTH_LONG).show();}
     }


    public void Iniciar(View view)
     {
         String email,password;
         email=this.email.getText().toString();
         password=this.password.getText().toString();
         try {
             if(!email.isEmpty()){
                 if(!password.isEmpty()){

                     validarUsuario();
                     //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                 }else{Toast.makeText(getApplicationContext(),"Este campo es requerido",Toast.LENGTH_SHORT).show();}
             }else{Toast.makeText(getApplicationContext(),"Este campo es requerido",Toast.LENGTH_SHORT).show();}
         }catch(Exception e){Toast.makeText(getApplicationContext(),"Ha ocurrido una excepcion al iniciar sesion",Toast.LENGTH_LONG).show();}
     }


    public void crearUsuario(View view)
    {
        startActivity(new Intent(getApplicationContext(),CrearUsuario.class));

    }// llave del metodo...

    public void validarUsuario(){
        String email,password;

        email=this.email.getText().toString();
        password=this.password.getText().toString();

            Camara camara = new Camara(getApplicationContext(),"db_camara",null,1);

            if(camara!=null){
                try {
                    SQLiteDatabase  sqLiteDatabase = camara.getReadableDatabase();
                    Cursor fila= sqLiteDatabase.rawQuery("select * from usuarios where correo_usuario='"+email+"'and password_usuario='"+password+"'", null);
                    if(fila.moveToFirst()){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else{Toast.makeText(getApplicationContext(),"a ocurrido un error ",Toast.LENGTH_SHORT).show();}

                }catch(Exception e){Toast.makeText(getApplicationContext(),"a ocrrido un error de excepcion al validar el usuario",Toast.LENGTH_SHORT).show();}


            }else{Toast.makeText(getApplicationContext(),"la bd no existe ",Toast.LENGTH_SHORT).show();}



    }// llave de validarUsuario....

}// llave de la clase..