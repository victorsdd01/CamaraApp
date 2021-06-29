package com.example.investigacion_camara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
    
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PERMISSION_CODE = 1000;
    private static final int PICK_IMAGE =1002;
    ImageView foto;
    Button btn_tomarFoto;

    /*
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String RutaImagen;
    */

     Uri uriImagen;
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

    public void tomarFoto(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                String[] permisos={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permisos,PERMISSION_CODE);
            }
            else{
                openCamera();
            }
        }
        else{
            openCamera();
        }
    }// llave del metodo....

    private void openCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Imagen Nueva");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Tomada desde la camara");
        uriImagen=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,uriImagen);
        startActivityForResult(i,IMAGE_CAPTURE_CODE);
    }

    public void openGallery(View view) throws IOException {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            foto.setImageURI(uriImagen);
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            uriImagen = data.getData();
            foto.setImageURI(uriImagen);
            Toast.makeText(this, "Foto desde la GALERIA", Toast.LENGTH_SHORT).show();
        }
    }


    /*
    //este metodo toma la foto....
    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                File imagenArchivo = null;
                try {
                    imagenArchivo=createImageFile();
                }catch (Exception e){Toast.makeText(getApplicationContext(),"error,en imagenArchivo",Toast.LENGTH_LONG).show();}

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
    */


}// lave de la clase