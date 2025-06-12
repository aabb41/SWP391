/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author nam
 */
public class ExportProduct {
    private UUID exportProductId;
    private  int ProductId;
    private int SupplierID;
    private int Quantity;
    private LocalDateTime CreataAt;
    private LocalDateTime date_of_export;
    private int status;

    public ExportProduct() {
    }

    public ExportProduct(UUID exportProductId, int ProductId, int SupplierID, int Quantity, LocalDateTime CreataAt, LocalDateTime date_of_export, int status) {
        this.exportProductId = exportProductId;
        this.ProductId = ProductId;
        this.SupplierID = SupplierID;
        this.Quantity = Quantity;
        this.CreataAt = CreataAt;
        this.date_of_export = date_of_export;
        this.status = status;
    }

    public UUID getExportProductId() {
        return exportProductId;
    }

    public void setExportProductId(UUID exportProductId) {
        this.exportProductId = exportProductId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int SupplierID) {
        this.SupplierID = SupplierID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public LocalDateTime getCreataAt() {
        return CreataAt;
    }

    public void setCreataAt(LocalDateTime CreataAt) {
        this.CreataAt = CreataAt;
    }

    public LocalDateTime getDate_of_export() {
        return date_of_export;
    }

    public void setDate_of_export(LocalDateTime date_of_export) {
        this.date_of_export = date_of_export;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ExportProduct{" + "exportProductId=" + exportProductId + ", ProductId=" + ProductId + ", SupplierID=" + SupplierID + ", Quantity=" + Quantity + ", CreataAt=" + CreataAt + ", date_of_export=" + date_of_export + ", status=" + status + '}';
    }
    
    
}
