/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;

/**
 *
 * @author nam
 */
public class Supppliers {
    private String Supplier_name;
    private String Phone;
    private String Address;
    private LocalDateTime createAt;
    private int Status;

    public Supppliers() {
    }

    public Supppliers(String Supplier_name, String Phone, String Address, LocalDateTime createAt, int Status) {
        this.Supplier_name = Supplier_name;
        this.Phone = Phone;
        this.Address = Address;
        this.createAt = createAt;
        this.Status = Status;
    }

    public String getSupplier_name() {
        return Supplier_name;
    }

    public void setSupplier_name(String Supplier_name) {
        this.Supplier_name = Supplier_name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "Supppliers{" + "Supplier_name=" + Supplier_name + ", Phone=" + Phone + ", Address=" + Address + ", createAt=" + createAt + ", Status=" + Status + '}';
    }
    
    
}
