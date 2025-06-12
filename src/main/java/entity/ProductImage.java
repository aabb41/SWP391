/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nam
 */
public class ProductImage {
    private int Productimg;
    private int ProductID;
    private String imgUrl;
    private int status;

    public ProductImage() {
    }

    public ProductImage(int Productimg, int ProductID, String imgUrl, int status) {
        this.Productimg = Productimg;
        this.ProductID = ProductID;
        this.imgUrl = imgUrl;
        this.status = status;
    }

    public int getProductimg() {
        return Productimg;
    }

    public void setProductimg(int Productimg) {
        this.Productimg = Productimg;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductImage{" + "Productimg=" + Productimg + ", ProductID=" + ProductID + ", imgUrl=" + imgUrl + ", status=" + status + '}';
    }
    
}
