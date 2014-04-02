package com.RAC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    private String currentContactName;
    private String currentContactNumber;
    private boolean randomContactChosen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void addRandomText(View v) {
        EditText textToSendBox = (EditText) findViewById(R.id.textToSend);
        ArrayList<String> niceTingsToSend = new ArrayList<String>();
        niceTingsToSend.add("You're beautiful!");
        niceTingsToSend.add("Have a splendid day!");
        niceTingsToSend.add("You are good at things!");
        niceTingsToSend.add("I hope you don't die soon!");
        Random r = new Random();
        String toSet = niceTingsToSend.get(r.nextInt(niceTingsToSend.size()));
        textToSendBox.setText(toSet);
    }


    public void openCamera(View v) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(columnIndex)));
            imageView.setVisibility(1);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setVisibility(1);
        }
    }

    public void randomContact(View v) {
        EditText randomContactBox = (EditText) findViewById(R.id.contact);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        //FROM STACK OVERFLOW
        // http://stackoverflow.com/questions/2356084/read-all-contacts-phone-numbers-in-android
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(new Contact(name,phoneNumber));
        }
        phones.close();
        Random r = new Random();
        Contact randomContact = contacts.get(r.nextInt(contacts.size()));
        String number = randomContact.getPhoneNumber();
        String name = randomContact.getName();
        randomContactBox.setText(name + " (" + number + ")");
        currentContactName = name;
        currentContactNumber = number;
        randomContactChosen = true;
    }
    public void sendMessage(View v) {
        EditText textToSendBox = (EditText) findViewById(R.id.textToSend);
        String messageToSend = textToSendBox.getText().toString();
        //Hardcoding in my number
      //  currentContactNumber = "9542961850";
        /*if (currentContactNumber=="none") {
            EditText phoneNumber   = (EditText)findViewById(R.id.contact);
            currentContactNumber = phoneNumber.getText().toString();
        }*/
        

        try {
            if (randomContactChosen) {
                SmsManager.getDefault().sendTextMessage(currentContactNumber, null, messageToSend, null,null);
            }
            else {
                EditText phoneNumberField   = (EditText)findViewById(R.id.contact);
                currentContactNumber = phoneNumberField.getText().toString(); 
                SmsManager.getDefault().sendTextMessage(currentContactNumber, null, messageToSend, null,null);
            }
            CharSequence text = "Message Sent!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show(); 
        
        } catch (Exception e) {
            CharSequence text = "Error: Message Not Sent!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show(); 
        }
        currentContactNumber="none";
       /* EditText phoneNumber   = (EditText)findViewById(R.id.contact);
        ((Editable) phoneNumber).clear(); */
        
     //   Context context = getApplicationContext();
    }
}

