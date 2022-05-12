package com.example.magicnote;

public class User {
    public String fullname,age,email;

    public User(){}
    public User(String fullname, String age, String email) {
        this.fullname = fullname;
        this.age = age;
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
