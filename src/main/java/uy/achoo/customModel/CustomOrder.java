package uy.achoo.customModel;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *         <p/>
 *         Order with custom fields. This is necesary because JOOQ's POJOs can only contain fields that are direclty mapped
 *         to physical database columns.
 */
public class CustomOrder {
    private static final long serialVersionUID = 1887060725;

    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "pharmacy_id")
    private Integer pharmacyId;
    @Column(name = "name")
    private String pharmacyName;
    @Column(name = "image_url")
    private String pharmacyImageUrl;
    @Column(name = "date")
    private Timestamp date;
    @Column(name = "score")
    private Integer score;

    private List<CustomOrderLine> orderLines;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Integer pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<CustomOrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<CustomOrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public String getPharmacyImageUrl() {
        return pharmacyImageUrl;
    }

    public void setPharmacyImageUrl(String pharmacyImageUrl) {
        this.pharmacyImageUrl = pharmacyImageUrl;
    }
}
