package mx.edu.ittepic.ladm_u4_ejercicio2_leercontactoscelular

import android.content.ContentResolver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val siLecturaContactos = 18
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),siLecturaContactos)
        }
        button.setOnClickListener {
            cargarListaContactos()
        }
    }
    private fun cargarListaContactos() {
        var resultado = ""
        val cursorContactos = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,null,null,null
        )
        if(cursorContactos!!.moveToFirst()){
            do{
            var idContacto = cursorContactos.getString(
                cursorContactos.getColumnIndex(ContactsContract.Contacts._ID))

            var nombreContacto = cursorContactos.getString(
                cursorContactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            )

            var telefonosContactos = ""
            if(cursorContactos.getInt(
                    cursorContactos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                var cursorCel = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf<String>(idContacto.toString()),null
                )

                while (cursorCel!!.moveToNext()){
                    telefonosContactos += cursorCel!!.getString(
                        cursorCel.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )+"\n"
                }
                cursorCel.close()
            }
                resultado += "ID: "+idContacto+"\nNombre: "+nombreContacto+"\nTelefonos:\n"+
                        telefonosContactos+"\n---------\n"
            }while (cursorContactos.moveToNext())

        }else{
            resultado ="CONTACTOS:\nNO HAY CONTACTOS CAPTURADOS"
        }
        textView.setText(resultado)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == siLecturaContactos){
            setTitle("PERMISO OTORGADO")
        }
    }
}
