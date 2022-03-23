package com.computerstore.csmp_sb.dao;
import com.computerstore.csmp_sb.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Dao {
    // Check if system statement is in OOTB or uninitialized
    public boolean isInitialized();

    // Set initialize status
    public int setInitialized();

    // Selling Parts and Computers
    public int sellPart(int amount, int skuId);
    public int sellComputer(int amount, int skuId);

    // Check password and auth
    public String checkAuth(String pass);

    // Change Password
    public int changePassword(String newPassword);

    // OOTB Add data
    public int addPart(Part part);
    public int addComputer(Computer computer);

    // Reset
    public int reset();

    // Show Inventory
    public List<Part> showInventory();

    // Refill items
    public int refillParts(int skuId, int amount);
    public int refillComputers(int skuId, int amount);

    // Edit price
    public int updatePartPrice(int skuId, double price);
    public int updateComputerPrice(int skuId, double price);

    // Create record
    public int addSellRecord(double price, int amount, int skuId, String time);
    public int addRefillRecord(double price, int amount, int skuId, String time);

    // Show Records
    public List<sellRecord> showRecords();

    // Check item available amount
    public int checkAvailable(int skuId);

    // Show Reports
    // Past 7 days Data
    public Integer show7dayAmount();
    public Double show7daySellingPrice();
    public Double show7dayRefillingPrice();

    // Past 30 days Data
    public Integer show30dayAmount();
    public Double show30daySellingPrice();
    public Double show30dayRefillingPrice();

    // Past All Data
    public Integer showAllAmount();
    public Double showAllSellingPrice();
    public Double showAllRefillingPrice();

}
