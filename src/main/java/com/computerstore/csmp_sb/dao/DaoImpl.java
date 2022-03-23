package com.computerstore.csmp_sb.dao;

import com.computerstore.csmp_sb.entity.*;
import com.computerstore.csmp_sb.mapper.SqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dao")
public class DaoImpl implements Dao {

    @Autowired
    private SqlMapper sqlMapper;

    @Override
    public boolean isInitialized() {
        if (sqlMapper.isInitialized() == 1) {
            return true;
        } else return false;
    }

    @Override
    public int setInitialized() {
        return sqlMapper.setInitialized();
    }

    // sell
    @Override
    public int sellPart(int amount, int skuId) {
        return sqlMapper.updateSellingPartAmount(amount, skuId);
    }

    @Override
    public int sellComputer(int amount, int skuId) {
        return sqlMapper.updateSellingComputerAmount(amount, skuId);
    }

    // check auth & password
    @Override
    public String checkAuth(String pass) {
        return sqlMapper.checkAuth(pass);
    }

    // change password
    @Override
    public int changePassword(String newPassword) {
        return sqlMapper.changePassword(newPassword);
    }

    // OOTB add data
    @Override
    public int addPart(Part part) {
        return sqlMapper.insertPart(part);
    }

    @Override
    public int addComputer(Computer computer) {
        return sqlMapper.insertComputer(computer);
    }


    // reset
    @Override
    public int reset() {
        int a = sqlMapper.resetInitStatus();
        int b = sqlMapper.deleteParts();
        int c = sqlMapper.deleteComputers();
        int d = sqlMapper.deleteSellRecords();
        int e = sqlMapper.deleteRefillRecords();
        int f = sqlMapper.resetSellAin();
        int g = sqlMapper.resetRefillAin();

        if (a == 1 && b == 1 && c == 1 && d == 1 && e == 1 && f == 1 && g == 1) {
            return 1;
        } else return 0;
    }

    // show inventory
    @Override
    public List<Part> showInventory() {
        return sqlMapper.showInventory();
    }

    // refill items
    @Override
    public int refillParts(int skuId, int amount) {
        return sqlMapper.refillParts(amount, skuId);
    }

    @Override
    public int refillComputers(int skuId, int amount) {
        return sqlMapper.refillComputers(amount, skuId);
    }

    // update price
    @Override
    public int updatePartPrice(int skuId, double price) {
        return sqlMapper.updatePart(skuId, price);
    }

    @Override
    public int updateComputerPrice(int skuId, double price) {
        return sqlMapper.updateComputer(skuId, price);
    }

    // add record
    @Override
    public int addSellRecord(double price, int amount, int skuId, String time) {
        return sqlMapper.addSellingRecord(price, amount, skuId, time);
    }

    @Override
    public int addRefillRecord(double price, int amount, int skuId, String time) {
        return sqlMapper.addRefillingRecord(price, amount, skuId, time);
    }

    @Override
    public List<sellRecord> showRecords() {
        return sqlMapper.showRecords();
    }

    @Override
    public int checkAvailable(int skuId) {
        return sqlMapper.checkAvailableAmount(skuId);
    }

    @Override
    public Integer show7dayAmount() {
        int a;
        try {
            a = sqlMapper.show7dayAmount();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Double show7daySellingPrice() {
        double a;
        try {
            a = sqlMapper.show7daySellingPrice();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Double show7dayRefillingPrice() {
        double a;
        try {
            a = sqlMapper.show7dayRefillingPrice();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Integer show30dayAmount() {
        int a;
        try {
            a = sqlMapper.show30dayAmount();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Double show30daySellingPrice() {
        double a;
        try {
            a = sqlMapper.show30daySellingPrice();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Double show30dayRefillingPrice() {
        double a;
        try {
            a = sqlMapper.show30dayRefillingPrice();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Integer showAllAmount() {
        int a;
        try {
            a = sqlMapper.showAllAmount();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Double showAllSellingPrice() {
        double a;
        try {
            a = sqlMapper.showAllSellingPrice();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

    @Override
    public Double showAllRefillingPrice() {
        double a;
        try {
            a = sqlMapper.showAllRefillingPrice();
        } catch (NullPointerException e) {
            a = 0;
        }
        return a;
    }

}
