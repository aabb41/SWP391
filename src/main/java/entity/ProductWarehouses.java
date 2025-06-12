/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.UUID;

/**
 *
 * @author nam
 */
public class ProductWarehouses {
    private UUID ProductWarehouses;
    private int ProductID;
    private int warehousesID;
    private int quantity;

    public ProductWarehouses() {
    }

    public ProductWarehouses(UUID ProductWarehouses, int ProductID, int warehousesID, int quantity) {
        this.ProductWarehouses = ProductWarehouses;
        this.ProductID = ProductID;
        this.warehousesID = warehousesID;
        this.quantity = quantity;
    }

    public UUID getProductWarehouses() {
        return ProductWarehouses;
    }

    public void setProductWarehouses(UUID ProductWarehouses) {
        this.ProductWarehouses = ProductWarehouses;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public int getWarehousesID() {
        return warehousesID;
    }

    public void setWarehousesID(int warehousesID) {
        this.warehousesID = warehousesID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductWarehouses{" + "ProductWarehouses=" + ProductWarehouses + ", ProductID=" + ProductID + ", warehousesID=" + warehousesID + ", quantity=" + quantity + '}';
    }
    
}
