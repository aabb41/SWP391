/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nam
 */
public class Category {
    private int CategoryId;
    private String CategoryName;
    private int Status;

    public Category() {
    }

    public Category(int CategoryId, String CategoryName, int Status) {
        this.CategoryId = CategoryId;
        this.CategoryName = CategoryName;
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "Category{" + "CategoryId=" + CategoryId + ", CategoryName=" + CategoryName + ", Status=" + Status + '}';
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
    
}
