/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author nam
 */
public class Product_Batches_Export {
    private UUID product_batches_id;
    private int product_id;
    private UUID batch_id;
    private int Quantity;
    private BigDecimal EntryPrice;
    private int status;

    public Product_Batches_Export() {
    }

    public Product_Batches_Export(UUID product_batches_id, int product_id, UUID batch_id, int Quantity, BigDecimal EntryPrice, int status) {
        this.product_batches_id = product_batches_id;
        this.product_id = product_id;
        this.batch_id = batch_id;
        this.Quantity = Quantity;
        this.EntryPrice = EntryPrice;
        this.status = status;
    }

    public UUID getProduct_batches_id() {
        return product_batches_id;
    }

    public void setProduct_batches_id(UUID product_batches_id) {
        this.product_batches_id = product_batches_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public UUID getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(UUID batch_id) {
        this.batch_id = batch_id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product_Batches_Export{" + "product_batches_id=" + product_batches_id + ", product_id=" + product_id + ", batch_id=" + batch_id + ", Quantity=" + Quantity + ", EntryPrice=" + EntryPrice + ", status=" + status + '}';
    }
    
    
}
