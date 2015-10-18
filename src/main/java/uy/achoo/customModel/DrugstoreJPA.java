package uy.achoo.customModel;

import uy.achoo.model.tables.pojos.Drugstore;

import javax.persistence.Column;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class DrugstoreJPA {
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

    public DrugstoreJPA() {}


    public DrugstoreJPA(
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

    public DrugstoreJPA(Drugstore drugstoreJooq) {
        this.setId(drugstoreJooq.getId());
        this.setName(drugstoreJooq.getName());
        this.setPhoneNumber(drugstoreJooq.getAddress());
        this.setAddress(drugstoreJooq.getAddress());
        this.setImageUrl(drugstoreJooq.getImageUrl());
    }


    public Long getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(Long distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
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
}
