package h5c.com.contactfetch;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ContactFetch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReadPhoneContacts(MainActivity.this);
    }

    public void ReadPhoneContacts(Context cntx) //This Context parameter is nothing but your Activity class's Context
    {
        Cursor cursor = cntx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        if (contactsCount > 0)
        {
            while(cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = cntx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext()) {
                        int phoneType 		= pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo 	= pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        switch (phoneType)
                        {
                            case Phone.TYPE_MOBILE:
                                Log.d(TAG,contactName + ": TYPE_MOBILE"+ " " + phoneNo);
                                break;
                            case Phone.TYPE_HOME:
                                Log.d(TAG,contactName + ": TYPE_HOME"+ " " + phoneNo);
                                break;
                            case Phone.TYPE_WORK:
                                Log.d(TAG,contactName + ": TYPE_WORK"+ " " + phoneNo);
                                break;
                            case Phone.TYPE_WORK_MOBILE:
                                Log.d(TAG,contactName + ": TYPE_WORK_MOBILE"+ " " + phoneNo);
                                break;
                            case Phone.TYPE_OTHER:
                                Log.d(TAG,contactName + ": TYPE_OTHER"+ " " + phoneNo);
                                break;
                            default:
                                break;
                        }
                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }
    }
}
