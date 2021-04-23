package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: cms_current_products_his
 * @Author: jeecg-boot
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public class CmsCurrentProductsHis implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String productionLineCode;

    private String productCode;
    private String batch;
    private String setter;

    private Date setterTime;

    private BigDecimal quantity;

    private String productName;

    private String specificationModel;

    private String unit;

    private String createBy;

    private Date createTime;
	/**更新人*/

    private String updateBy;

    private Date updateTime;

    private String equipmentId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public Date getSetterTime() {
        return setterTime;
    }

    public void setSetterTime(Date setterTime) {
        this.setterTime = setterTime;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecificationModel() {
        return specificationModel;
    }

    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
