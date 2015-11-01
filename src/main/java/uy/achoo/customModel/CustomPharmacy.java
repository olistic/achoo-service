package uy.achoo.customModel;

import uy.achoo.model.tables.pojos.Pharmacy;

import javax.persistence.Column;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Pharmacy with custom fields. This is necesary because JOOQ's POJOs can only contain fields that are direclty mapped
 * to physical database columns.
 */
public class CustomPharmacy {
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

    @Column (name = "average_score")
    private Double score;

    private Long distanceFromOrigin;


    public CustomPharmacy() {}


    public CustomPharmacy(Integer id, String name, String phoneNumber, String address, String imageUrl) {
        this.setId(id);
        this.setName(name);
        this.setPhoneNumber(phoneNumber);
        this.setAddress(address);
        this.setImageUrl(imageUrl);
    }

    public CustomPharmacy(Pharmacy pharmacyJooq) {
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

    public Double getScore() {return score;}

    public void setScore(Double score) {this.score = score;}

    public Long getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(Long distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }
}
