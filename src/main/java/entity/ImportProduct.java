/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

/**
 *
 * @author nam
 */
public class ImportProduct {
    private UUID importProduct;
    private int ProductId;
    private int Supplier;
    private int Quantity;
    private BigDecimal  EntryPrice;
    private LocalTime createAt;
    private BigDecimal  import_price;
    private LocalTime date_of_import;
    private int status;

    public ImportProduct() {
    }

    public ImportProduct(UUID importProduct, int ProductId, int Supplier, int Quantity, BigDecimal EntryPrice, LocalTime createAt, BigDecimal import_price, LocalTime date_of_import, int status) {
        this.importProduct = importProduct;
        this.ProductId = ProductId;
        this.Supplier = Supplier;
        this.Quantity = Quantity;
        this.EntryPrice = EntryPrice;
        this.createAt = createAt;
        this.import_price = import_price;
        this.date_of_import = date_of_import;
        this.status = status;
    }

    public UUID getImportProduct() {
        return importProduct;
    }

    public void setImportProduct(UUID importProduct) {
        this.importProduct = importProduct;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public int getSupplier() {
        return Supplier;
    }

    public void setSupplier(int Supplier) {
        this.Supplier = Supplier;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public BigDecimal getEntryPrice() {
        return EntryPrice;
    }

    public void setEntryPrice(BigDecimal EntryPrice) {
        this.EntryPrice = EntryPrice;
    }

    public LocalTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalTime createAt) {
        this.createAt = createAt;
    }

    public BigDecimal getImport_price() {
        return import_price;
    }

    public void setImport_price(BigDecimal import_price) {
        this.import_price = import_price;
    }

    public LocalTime getDate_of_import() {
        return date_of_import;
    }

    public void setDate_of_import(LocalTime date_of_import) {
        this.date_of_import = date_of_import;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ImportProduct{" + "importProduct=" + importProduct + ", ProductId=" + ProductId + ", Supplier=" + Supplier + ", Quantity=" + Quantity + ", EntryPrice=" + EntryPrice + ", createAt=" + createAt + ", import_price=" + import_price + ", date_of_import=" + date_of_import + ", status=" + status + '}';
    }
    
    
    
}
