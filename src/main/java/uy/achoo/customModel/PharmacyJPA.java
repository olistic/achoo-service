package uy.achoo.customModel;

import uy.achoo.model.tables.pojos.Pharmacy;

import javax.persistence.Column;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class PharmacyJPA {
    private static final long serialVersionUID = 1785879361;

    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name= "image_url")
    private String imageUrl;


    private Long distanceFromOrigin;

    @Column (name = "average_score")
    private Double averageScore;

    public PharmacyJPA() {}


    public PharmacyJPA(
            Integer id,
            String name,
            String phoneNumber,
            String address,
            String imageUrl
    ) {
        this.setId(id);
        this.setName(name);
        this.setPhoneNumber(phoneNumber);
        this.setAddress(address);
        this.setImageUrl(imageUrl);
    }

    public PharmacyJPA(Pharmacy pharmacyJooq) {
        this.setId(pharmacyJooq.getId());
        this.setName(pharmacyJooq.getName());
        this.setPhoneNumber(pharmacyJooq.getAddress());
        this.setAddress(pharmacyJooq.getAddress());
        this.setImageUrl(pharmacyJooq.getImageUrl());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public Double getAverageScore() {return averageScore;}

    public void setAverageScore(Double averageScore) {this.averageScore = averageScore;}

    public Long getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(Long distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }
}
