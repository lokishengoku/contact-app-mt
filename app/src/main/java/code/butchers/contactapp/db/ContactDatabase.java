package code.butchers.contactapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import code.butchers.contactapp.models.Contact;

@Database( entities = Contact.class, version = 1)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
    public static ContactDatabase instance;

    public static ContactDatabase getInstance(Context mContext){
        if (instance == null){
            instance = Room.databaseBuilder(mContext,
                    ContactDatabase.class, "database-contact")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    };
}
