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
    private Integer drugstoreId;
    @Column(name = "name")
    private String drugstoreName;
    @Column(name = "phone_number")
    private String drugstorePhoneNumber;
    @Column(name = "address")
    private String drugstoreAddress;

    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_description")
    private String productDescription;
    @Column(name = "product_unitary_price")
    private Double productUnitaryPrice;
    @Column(name = "product_image_url")
    private String productImageUrl;

    private Long distanceFromOrigin;

    public DrugstoreJPA() {}


    public DrugstoreJPA(
            Integer id,
            String name,
            String phoneNumber,
            String address
    ) {
        this.setDrugstoreId(id);
        this.setDrugstoreName(name);
        this.setDrugstorePhoneNumber(phoneNumber);
        this.setDrugstoreAddress(address);
    }

    public DrugstoreJPA(Drugstore drugstoreJooq) {
        this.setDrugstoreId(drugstoreJooq.getId());
        this.setDrugstoreName(drugstoreJooq.getName());
        this.setDrugstorePhoneNumber(drugstoreJooq.getAddress());
        this.setDrugstoreAddress(drugstoreJooq.getAddress());
    }




    public Long getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(Long distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getDrugstoreId() {
        return drugstoreId;
    }

    public void setDrugstoreId(Integer drugstoreId) {
        this.drugstoreId = drugstoreId;
    }

    public String getDrugstoreName() {
        return drugstoreName;
    }

    public void setDrugstoreName(String drugstoreName) {
        this.drugstoreName = drugstoreName;
    }

    public String getDrugstorePhoneNumber() {
        return drugstorePhoneNumber;
    }

    public void setDrugstorePhoneNumber(String drugstorePhoneNumber) {this.drugstorePhoneNumber = drugstorePhoneNumber;}

    public String getDrugstoreAddress() {
        return drugstoreAddress;
    }

    public void setDrugstoreAddress(String drugstoreAddress) {
        this.drugstoreAddress = drugstoreAddress;
    }

    public Double getProductUnitaryPrice() {
        return productUnitaryPrice;
    }

    public void setProductUnitaryPrice(Double productUnitaryPrice) {
        this.productUnitaryPrice = productUnitaryPrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductImageUrl() {return productImageUrl;}

    public void setProductImageUrl(String productImageUrl) {this.productImageUrl = productImageUrl;}
}
