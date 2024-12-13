package com.cs4411;

public class User {
    public final Long userID;
    private String userName;
    public User(Long uidBase, String name){
        this.userName = name;
        this.userID = uidBase;

    }
    public String getName(){
        return new String(this.userName);
    }
    public void setName(String n){
        this.userName = new String(n);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof User)){
            return false;
        }
        else{
            if (((User) o).userID == this.userID && ((User) o).userName == this.userName){
                return true;
            }
            else{
                return false;
            }
        }
    }
}