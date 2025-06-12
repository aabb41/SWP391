/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author nam
 */
public class Products {
    private int ProductId;
    private String ProductName;
    private BigDecimal EntryPrice;        
    private BigDecimal RetailPrice;        
    private String Description;        
    private BigDecimal WHOSALEPRICE;        
    private String Unit;        
    private int Quantity;
    private int CategoryId;        
    private int SupplierID;
    private LocalDateTime CreateAt;        
    private LocalDateTime Update;        
    private int status;        

    public Products() {
    }

    public Products(int ProductId, String ProductName, BigDecimal EntryPrice, BigDecimal RetailPrice, String Description, BigDecimal WHOSALEPRICE, String Unit, int Quantity, int CategoryId, int SupplierID, LocalDateTime CreateAt, LocalDateTime Update, int status) {
        this.ProductId = ProductId;
        this.ProductName = ProductName;
        this.EntryPrice = EntryPrice;
        this.RetailPrice = RetailPrice;
        this.Description = Description;
        this.WHOSALEPRICE = WHOSALEPRICE;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.CategoryId = CategoryId;
        this.SupplierID = SupplierID;
        this.CreateAt = CreateAt;
        this.Update = Update;
        this.status = status;
    }

    
    

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public BigDecimal getEntryPrice() {
        return EntryPrice;
    }

    public void setEntryPrice(BigDecimal EntryPrice) {
        this.EntryPrice = EntryPrice;
    }

    public BigDecimal getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(BigDecimal RetailPrice) {
        this.RetailPrice = RetailPrice;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public BigDecimal getWHOSALEPRICE() {
        return WHOSALEPRICE;
    }

    public void setWHOSALEPRICE(BigDecimal WHOSALEPRICE) {
        this.WHOSALEPRICE = WHOSALEPRICE;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int CategoryId) {
        this.CategoryId = CategoryId;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int SupplierID) {
        this.SupplierID = SupplierID;
    }

    public LocalDateTime  getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(LocalDateTime  CreateAt) {
        this.CreateAt = CreateAt;
    }

    public LocalDateTime  getUpdate() {
        return Update;
    }

    public void setUpdate(LocalDateTime  Update) {
        this.Update = Update;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Products{" + "ProductId=" + ProductId + ", ProductName=" + ProductName + ", EntryPrice=" + EntryPrice + ", RetailPrice=" + RetailPrice + ", Description=" + Description + ", WHOSALEPRICE=" + WHOSALEPRICE + ", Unit=" + Unit + ", Quantity=" + Quantity + ", CategoryId=" + CategoryId + ", SupplierID=" + SupplierID + ", CreateAt=" + CreateAt + ", Update=" + Update + ", status=" + status + '}';
    }

    
}
