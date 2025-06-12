/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author nam
 */
public class Warehouses {
    private String warehouses_name;
    private String location;
    private LocalDateTime createAt;
    private int Status;

    public Warehouses() {
    }

    public Warehouses(String warehouses_name, String location, LocalDateTime createAt, int Status) {
        this.warehouses_name = warehouses_name;
        this.location = location;
        this.createAt = createAt;
        this.Status = Status;
    }

    public String getWarehouses_name() {
        return warehouses_name;
    }

    public void setWarehouses_name(String warehouses_name) {
        this.warehouses_name = warehouses_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        return "Warehouses{" + "warehouses_name=" + warehouses_name + ", location=" + location + ", createAt=" + createAt + ", Status=" + Status + '}';
    }
}
