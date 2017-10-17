package com.example.consumer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Consumer {
    private int memberId;
    private String fullName;

    public Consumer(int personId, String firstName, String lastName) {
        this.memberId = personId;
        this.fullName = firstName + " " + lastName;
    }
}