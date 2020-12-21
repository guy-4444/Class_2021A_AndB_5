//package com.classy.class_2021a_andb_5.user;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "users")
//public class User {
//
//    @PrimaryKey(autoGenerate = true)
//    public int id;
//
//    @ColumnInfo(name = "uuid")
//    public String uuid;
//
//    @ColumnInfo(name = "first_name")
//    public String firstName;
//
//    @ColumnInfo(name = "last_name")
//    public String lastName;
//
//    public User() { }
//
//    @Ignore
//    public User(String uuid, String firstName, String lastName) {
//        this.uuid = uuid;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//
//    @Ignore
//    public User(int id, String uuid, String firstName, String lastName) {
//        this.id = id;
//        this.uuid = uuid;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//}