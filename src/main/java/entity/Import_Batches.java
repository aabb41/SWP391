/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 *
 * @author nam
 */
public class Import_Batches {
    private UUID batch_id;
    private int SupplierID;
    private LocalDateTime createAt;
    private BigDecimal TotalPrice;
    private LocalDateTime date_of_import;
    private int status;

    public Import_Batches() {
    }

    public Import_Batches(UUID batch_id, int SupplierID, LocalDateTime createAt, BigDecimal TotalPrice, LocalDateTime date_of_import, int status) {
        this.batch_id = batch_id;
        this.SupplierID = SupplierID;
        this.createAt = createAt;
        this.TotalPrice = TotalPrice;
        this.date_of_import = date_of_import;
        this.status = status;
    }

    
    public UUID getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(UUID batch_id) {
        this.batch_id = batch_id;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int SupplierID) {
        this.SupplierID = SupplierID;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public BigDecimal getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(BigDecimal TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public LocalDateTime getDate_of_import() {
        return date_of_import;
    }

    public void setDate_of_import(LocalDateTime date_of_import) {
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
        return "Import_Batches{" + "batch_id=" + batch_id + ", SupplierID=" + SupplierID + ", createAt=" + createAt + ", TotalPrice=" + TotalPrice + ", date_of_import=" + date_of_import + ", status=" + status + '}';
    }
    
    
}
