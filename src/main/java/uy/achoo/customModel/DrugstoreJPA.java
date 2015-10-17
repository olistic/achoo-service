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

    @Column(name ="id")
    private Integer id;
    @Column(name ="name")
    private String  name;
    @Column(name ="phone_number")
    private String  phoneNumber;
    @Column(name ="address")
    private String  address;

    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_description")
    private String productDescription;
    @Column(name = "product_unitary_price")
    private Double unitaryPrice;

    private Long distanceFromOrigin;

    public DrugstoreJPA() {}

    public DrugstoreJPA(DrugstoreJPA value) {
        this.id = value.id;
        this.name = value.name;
        this.phoneNumber = value.phoneNumber;
        this.address = value.address;
    }

    public DrugstoreJPA(
            Integer id,
            String  name,
            String  phoneNumber,
            String  address
    ) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public DrugstoreJPA(Drugstore drugstoreJooq){
        this.id = drugstoreJooq.getId();
        this.name = drugstoreJooq.getName();
        this.phoneNumber = drugstoreJooq.getAddress();
        this.address = drugstoreJooq.getAddress();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDistanceFromOrigin() {return distanceFromOrigin;}

    public void setDistanceFromOrigin(Long distanceFromOrigin) {this.distanceFromOrigin = distanceFromOrigin;}

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public Double getUnitaryPrice() {return unitaryPrice;}

    public void setUnitaryPrice(Double unitaryPrice) {this.unitaryPrice = unitaryPrice;}

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
