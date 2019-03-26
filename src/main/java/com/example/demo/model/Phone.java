package com.example.demo.model;


import javax.persistence.*;
import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "number")
    private int number;

    @Column(name = "person_id")
    private Long personId;


//    @ManyToOne
//    @JoinColumn(name = "person_id", insertable = false, updatable = false)
//    Person person;

//    @OneToOne(mappedBy = "phone")
//    private Person person;


    public Phone(String type, int number, Long personId) {
        this.type = type;
        this.number=number;
        this.personId=personId;
    }
    public Phone(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

//    public Person getPerson() {
//        return person;
//    }
//
//    public void setPerson(Person person) {
//        this.person = person;
//    }
}


