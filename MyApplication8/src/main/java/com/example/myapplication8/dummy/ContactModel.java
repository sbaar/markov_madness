package com.example.myapplication8.dummy;

/**
 * Created by SBaar on 1/12/14.
 */
public class ContactModel {
    public ContactModel(String n, String num, String i){
        name = n;
        number = num;
        id = i;
    }
    public boolean isAll = false;
    private String name;
    public String number;
    public String id;
    @Override
 public String toString(){
     return name + " " + number;
 }
}
