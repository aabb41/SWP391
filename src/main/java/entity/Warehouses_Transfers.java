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
public class Warehouses_Transfers {
    private UUID transfer_id;
    private int ProductId;
    private int Quantity;
    private LocalDateTime createAt;
    private int From_warehouse_Id;
    private int To_warehouse_Id;
    private LocalDateTime date_of_transfer;
    private int status;

    public Warehouses_Transfers() {
    }

    public Warehouses_Transfers(UUID transfer_id, int ProductId, int Quantity, LocalDateTime createAt, int From_warehouse_Id, int To_warehouse_Id, LocalDateTime date_of_transfer, int status) {
        this.transfer_id = transfer_id;
        this.ProductId = ProductId;
        this.Quantity = Quantity;
        this.createAt = createAt;
        this.From_warehouse_Id = From_warehouse_Id;
        this.To_warehouse_Id = To_warehouse_Id;
        this.date_of_transfer = date_of_transfer;
        this.status = status;
    }

    public UUID getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(UUID transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public int getFrom_warehouse_Id() {
        return From_warehouse_Id;
    }

    public void setFrom_warehouse_Id(int From_warehouse_Id) {
        this.From_warehouse_Id = From_warehouse_Id;
    }

    public int getTo_warehouse_Id() {
        return To_warehouse_Id;
    }

    public void setTo_warehouse_Id(int To_warehouse_Id) {
        this.To_warehouse_Id = To_warehouse_Id;
    }

    public LocalDateTime getDate_of_transfer() {
        return date_of_transfer;
    }

    public void setDate_of_transfer(LocalDateTime date_of_transfer) {
        this.date_of_transfer = date_of_transfer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Warehouses{" + "transfer_id=" + transfer_id + ", ProductId=" + ProductId + ", Quantity=" + Quantity + ", createAt=" + createAt + ", From_warehouse_Id=" + From_warehouse_Id + ", To_warehouse_Id=" + To_warehouse_Id + ", date_of_transfer=" + date_of_transfer + ", status=" + status + '}';
    }
}
