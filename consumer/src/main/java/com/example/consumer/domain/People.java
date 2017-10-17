package com.example.consumer.domain;

import lombok.Data;

@Data
public class People {
    private int personId;
    private String firstName;
    private String lastName;
    private String fullName;


    public People(int personId, String firstName, String lastName) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        setFullName();
    }

    public void setFullName(){
        this.fullName = firstName + " " + lastName;
    }
}
