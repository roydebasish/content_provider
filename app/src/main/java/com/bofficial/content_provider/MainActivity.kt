package com.bofficial.content_provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.bofficial.content_provider.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val list: MutableLiveData<MutableList<Contacts>> = MutableLiveData()


    private val registerActivityForResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                list.postValue(getContactList())
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.postValue(getContactList())

        list.observe(this){
            it?.let {
                binding.rvContacts.adapter = MainAdapter(it)
            }
        }
    }


    @SuppressLint("Range", "Recycle")
    fun getContactList():MutableList<Contacts>{
        val list = mutableListOf<Contacts>()
        sdkIntAboveOreo {

            isPermissionGranted(this,android.Manifest.permission.READ_CONTACTS){
                if(it){
                    val contentResolver = applicationContext.contentResolver
                    val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null)
                    if(cursor?.moveToFirst()==true){
                        do {
                            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            val number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            list.add(Contacts(name,number))
                        }while(cursor.moveToNext())

                    }
                }else{
                    registerActivityForResult.launch(android.Manifest.permission.READ_CONTACTS)
                }

            }


        }
        return list
    }


    inline fun sdkIntAboveOreo(call: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            call.invoke()
        }
    }

    inline fun isPermissionGranted(context: Context, permission: String, call: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            call.invoke(true)
        } else {
            call.invoke(false)
        }

    }

}