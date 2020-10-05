package code.butchers.contactapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import code.butchers.contactapp.models.Contact;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM table_contact")
    List<Contact> getAllContacts();

    @Insert
    void insertContact(Contact... contact);

    @Update
    void updateContact(Contact... contact);

    @Delete
    void deleteContact(Contact... contact);
}
