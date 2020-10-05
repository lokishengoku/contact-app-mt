package code.butchers.contactapp.models;

import android.graphics.Color;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Random;

@Entity(tableName = "table_contact")
public class Contact implements Serializable {
    @PrimaryKey( autoGenerate = true)
    @ColumnInfo( name = "cid")
    private  int cid;

    @ColumnInfo( name = "fullName")
    private String fullName;

    @ColumnInfo( name = "phoneNumber")
    private String phoneNumber;

    @ColumnInfo( name = "email")
    private String email;

    @ColumnInfo( name = "firstChar")
    private String firstChar;

    @ColumnInfo( name = "detail")
    private String detail;

    @ColumnInfo( name = "hasImg")
    private boolean hasImg;

    @ColumnInfo( name = "avatarColor")
    private int avatarColor;

    @ColumnInfo( name = "avatarImg")
    private String avatarImg;

    public Contact(String fullName, String phoneNumber, String email, String detail) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.firstChar = String.valueOf(fullName.charAt(0));
        this.detail = detail;

        hasImg = false;
        Random rnd = new Random();
        this.avatarColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        this.avatarImg = "";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAvatarColor() {
        return avatarColor;
    }

    public void setAvatarColor(int avatarColor) {
        this.avatarColor = avatarColor;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public boolean isHasImg() {
        return hasImg;
    }

    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }
}
