package com.example.kpo.DataType;

import java.util.Date;

public class Data {
    private int id;
    private String name;
    private String brand;
    private String fullName;
    private String numPhone;
    private int count;
    private Date dateAccept;
    private Date dateIssue;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNumPhone() {
        return numPhone;
    }

    public int getCount() {
        return count;
    }

    public Date getDateAccept() {
        return dateAccept;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public String getStatus() {
        return status;
    }
    public Data(int id, String name, String brand, String fullName, String numPhone, int count, Date dateAceept, Date dateIssue, String status) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.fullName = fullName;
        this.numPhone = numPhone;
        this.count = count;
        this.dateAccept = dateAceept;
        this.dateIssue = dateIssue;
        this.status = status;
    }
}
