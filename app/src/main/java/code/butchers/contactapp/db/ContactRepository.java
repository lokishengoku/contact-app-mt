package code.butchers.contactapp.db;
import android.app.Application;
import android.os.AsyncTask;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import code.butchers.contactapp.models.Contact;

public class ContactRepository implements Serializable {
    private static ContactDatabase contactDatabase;
    private static ContactDao contactDao;
    private static int previousExpandId = -1;


    public ContactRepository(Application application){
        contactDatabase = ContactDatabase.getInstance(application);
        contactDao = contactDatabase.contactDao();
    }

    public void insertContact(Contact newContact){
        new InsertContactAsyncTask(contactDao).execute(newContact);
    }

    public void updateContact(Contact contact){
        new UpdateContactAsyncTask(contactDao).execute(contact);
    }

    public void deleteContact(Contact contact){
        previousExpandId = -1;
        new DeleteContactAsyncTask(contactDao).execute(contact);
    }

    public static int getPreviousExpandId() {
        return previousExpandId;
    }

    public static void setPreviousExpandId(int previousExpandId) {
        ContactRepository.previousExpandId = previousExpandId;
    }

    public ContactDatabase getContactDatabase() {
        return contactDatabase;
    }

    public void setContactDatabase(ContactDatabase contactDatabase) {
        this.contactDatabase = contactDatabase;
    }

    public ContactDao getContactDao() {
        return contactDao;
    }

    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void>{
        private ContactDao contactDao;
        private  InsertContactAsyncTask(ContactDao contactDao){
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.insertContact(contacts[0]);
            return null;
        }
    }

    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void>{
        private ContactDao contactDao;
        private  UpdateContactAsyncTask(ContactDao contactDao){
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.updateContact(contacts[0]);
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void>{
        private ContactDao contactDao;
        private  DeleteContactAsyncTask(ContactDao contactDao){
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.deleteContact(contacts[0]);
            return null;
        }
    }

}
