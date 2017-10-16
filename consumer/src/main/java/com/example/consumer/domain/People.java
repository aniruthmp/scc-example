package com.example.consumer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {
    private String firstName;
    private String lastName;

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
