package com.example.investigacion_camara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    ImageView foto;
    Button btn_tomarFoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String RutaImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IniciarController();
    }
    private void IniciarController(){
        try {
            foto=(ImageView)findViewById(R.id.Main_foto);
            btn_tomarFoto=(Button)findViewById(R.id.btn_tomarFoto);
            btn_tomarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tomarFoto();
                }
            });
        }catch (Exception e){Toast.makeText(getApplicationContext(),"A ocurrido una excepcion al cargar el controlador", Toast.LENGTH_LONG).show();}


    }// llave del metodo controller...

    //este metodo toma la foto....
    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                File imagenArchivo = null;
                try {
                    imagenArchivo=createImageFile();
                }catch (Exception e){}

                if(imagenArchivo !=null){
                    Uri fotoUri = FileProvider.getUriForFile(this,"com.example.investigacion_camara",imagenArchivo);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                }

            }else{Toast.makeText(getApplicationContext(),"error, tu dispositivo no tiene camara",Toast.LENGTH_LONG).show();}
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),"error al tomar la foto",Toast.LENGTH_LONG).show();
        }
    }
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            Bitmap imageBitmap = BitmapFactory.decodeFile(RutaImagen);//(Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
        }
    }
    //este metodo crea un backup de las images...
    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "foto_";
        File Directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",Directorio);

        // Save a file: path for use with ACTION_VIEW intents
        RutaImagen = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // esta es la condicion que valida si el dispositivo tiene camara...
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
               Toast.makeText(getApplicationContext(),"Error photoFile=null",Toast.LENGTH_LONG).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.investigacion_camara.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }catch (Exception e){Toast.makeText(getApplicationContext(),"Error photoFile != null",Toast.LENGTH_LONG).show();}
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(RutaImagen);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



}// lave de la clase