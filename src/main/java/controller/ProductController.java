/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Products;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Vector;
import model.DAOProducts;

/**
 *
 * @author nam
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductURL"})
public class ProductController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DAOProducts dao = new DAOProducts();
        try (PrintWriter out = response.getWriter()) {
            String service = request.getParameter("service");
            if (service == null) {
                service = "listProduct";
            }
            if (service.equals("listProduct")) {
                Vector<Products> vector;
                String submit = request.getParameter("submit");
                if (submit == null) { // hiển thị tất cả
                    vector = dao.getProducts("select * from Products");
                    System.out.println("Number of products retrieved: " + vector.size()); // Debug
                    for (Products p : vector) {
                        System.out.println(p); // Debug
                    }
                }else { // tìm kiếm
                    String searchField = request.getParameter("searchField");
                    String searchValue = request.getParameter("searchValue");
                    
                    String sql = "select * from Products where 1=1";
                    if (searchField != null && !searchField.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
                        if (searchField.equals("ProductId")) {
                            sql += " AND ProductId = " + searchValue;
                        } else if (searchField.equals("ProductName")) {
                            sql += " AND ProductName like N'%" + searchValue + "%'";
                        } else if (searchField.equals("SupplierID")) {
                            sql += " AND SupplierID = " + searchValue;
                        } else if (searchField.equals("CategoryId")) {
                            sql += " AND CategoryId = " + searchValue;
                        } else if (searchField.equals("Unit")) {
                            sql += " AND Unit like N'%" + searchValue + "%'";
                        } else if (searchField.equals("PriceRange")) {
                            String[] priceRange = searchValue.split("-");
                            if (priceRange.length == 2) {
                                sql += " AND RetailPrice BETWEEN " + priceRange[0] + " AND " + priceRange[1];
                            }
                        }
                    }
                    vector = dao.getProducts(sql);
                }
                
                request.setAttribute("productData", vector);
                request.setAttribute("titleTable", "Danh sách sản phẩm");
                
                RequestDispatcher dis = request.getRequestDispatcher("/jsp/listProduct.jsp");
                dis.forward(request, response);
            }
            
            if (service.equals("insertProduct")) {
                String submit = request.getParameter("submit");
                if (submit == null) { //display form
                    request.getRequestDispatcher("/jsp/insertProduct.jsp").forward(request, response);
                } else { //insert
                    int ProductId = Integer.parseInt(request.getParameter("ProductId"));
                    String ProductName = request.getParameter("ProductName");
                    BigDecimal EntryPrice = new BigDecimal(request.getParameter("EntryPrice"));
                    BigDecimal RetailPrice = new BigDecimal(request.getParameter("RetailPrice"));
                    String Description = request.getParameter("Description");
                    BigDecimal WHOSALEPRICE = new BigDecimal(request.getParameter("WHOSALEPRICE"));
                    String Unit = request.getParameter("Unit");
                    int Quantity = Integer.parseInt(request.getParameter("Quantity"));
                    int CategoryId = Integer.parseInt(request.getParameter("CategoryId"));
                    int SupplierID = Integer.parseInt(request.getParameter("SupplierID"));
                    LocalDateTime CreateAt = LocalDateTime.now();
                    LocalDateTime Update = CreateAt;
                    int status = Integer.parseInt(request.getParameter("status"));
                    
                    Products pro = new Products(ProductId, ProductName, EntryPrice, RetailPrice, Description, WHOSALEPRICE, Unit, Quantity, CategoryId, SupplierID, CreateAt, Update, status);
                    dao.addProduct(pro);
                    response.sendRedirect("ProductURL");
                }
            }
            
            
            
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
