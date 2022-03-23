package com.computerstore.csmp_sb.mapper;

import com.computerstore.csmp_sb.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SqlMapper {
    // Check password and auth
    @Select("select * from password where pass=#{auth_password}")
    @Options(useCache = false)
    public String checkAuth(String pass);

    // Check if OOTB status is initialized
    @Select("select value from settings where sid = 1;")
    @Options(useCache = true)
    public int isInitialized();

    // Set initialized status while OOTB has finished
    @Update("update settings set value = 1 where sid = 1")
    public int setInitialized();

    // Change Password
    @Update("update password set pass = #{auth_new_password}")
    public int changePassword(String newPassword);

    // Refilling parts & computers
    @Update("update part set amount=amount+#{amount} where skuID=#{skuId}")
    public int refillParts(@Param("amount") int amount, @Param("skuId") int skuId);

    @Update("update computer set amount=amount+#{amount} where skuId=#{skuId}")
    public int refillComputers(@Param("amount") int amount, @Param("skuId") int skuId);

    // Selling parts & computers
    @Update("update part set amount=amount-#{amount} where skuID=#{skuId}")
    public int updateSellingPartAmount(@Param("amount") int amount, @Param("skuId") int skuId);

    @Update("update computer set amount=amount-#{amount} where skuID=#{skuId}")
    public int updateSellingComputerAmount(@Param("amount") int amount, @Param("skuId") int skuId);

    // Check available item amount
    @Select("select amount from part where skuID=#{skuId} union select amount from computer where skuId=#{skuId}")
    public int checkAvailableAmount(@Param("skuId") int skuId);

    // Create Records when selling & refilling
    @Insert("insert into sellingRecords (price, amount, skuId, dateTime, type) VALUES (#{price}, #{amount}, #{skuId}, #{dateTime}, 'Sell')")
    public int addSellingRecord(@Param("price") double price, @Param("amount") int amount, @Param("skuId") int skuId, @Param("dateTime") String dateTime);

    @Insert("insert into refillRecords (price, amount, skuId, dateTime, type) VALUES (#{price}, #{amount}, #{skuId}, #{dateTime}, 'Refill')")
    public int addRefillingRecord(@Param("price") double price, @Param("amount") int amount, @Param("skuId") int skuId, @Param("dateTime") String dateTime);

    // OOTB Add Data
    @Insert("insert into part (skuID, name, amount, price) values (#{skuId}, #{name}, #{amount}, #{price});")
    public int insertPart(Part part);

    @Insert("insert into computer (skuID, name, amount, price) values (#{skuId}, #{name}, #{amount}, #{price});")
    public int insertComputer(Computer computer);

    // Reset
    @Update("update settings set value = 0 where sid = 1")
    public int resetInitStatus();

    @Delete("delete from part where 1=1")
    public int deleteParts();

    @Delete("delete from computer where 1=1")
    public int deleteComputers();

    @Delete("delete from sellingRecords where 1=1")
    public int deleteSellRecords();

    @Update("alter table sellingRecords auto_increment=10000000")
    public int resetSellAin();

    @Delete("delete from refillRecords where 1=1;")
    public int deleteRefillRecords();

    @Update("alter table refillRecords auto_increment=60000000")
    public int resetRefillAin();

    // Show Inventory
    @Select("select * from part union select * from computer")
    public List<Part> showInventory();

    // Change Item Price
    @Update("update part set price = #{price} where skuID = #{skuId}")
    public int updatePart(@Param("skuId") int skuId, @Param("price") double price);

    @Update("update computer set price=#{price} where skuId=#{skuId}")
    public int updateComputer(@Param("skuId") int skuId, @Param("price") double price);

    // Show Records
    @Select("select * from sellingRecords union select * from refillRecords order by dateTime desc")
    public List<sellRecord> showRecords();

    // Show Report
    // Past 7 days Data
    @ResultType(Integer.class)
    @Select("select SUM(amount) from sellingRecords where dateTime between concat((subdate(curdate(), 7)), ' 00:00:00') and concat(curdate(), ' 23:59:59') order by dateTime desc")
    public Integer show7dayAmount();
    @ResultType(Double.class)
    @Select("select SUM(price) from sellingRecords where dateTime between concat((subdate(curdate(), 7)), ' 00:00:00') and concat(curdate(), ' 23:59:59') order by dateTime desc")
    public Double show7daySellingPrice();
    @ResultType(Double.class)
    @Select("select SUM(price) from refillRecords where dateTime between concat((subdate(curdate(), 7)), ' 00:00:00') and concat(curdate(), ' 23:59:59') order by dateTime desc")
    public Double show7dayRefillingPrice();

    // Past 30 days Data
    @ResultType(Integer.class)
    @Select("select SUM(amount) from sellingRecords where dateTime between concat((subdate(curdate(), 30)), ' 00:00:00') and concat(curdate(), ' 23:59:59') order by dateTime desc")
    public Integer show30dayAmount();
    @ResultType(Double.class)
    @Select("select SUM(price) from sellingRecords where dateTime between concat((subdate(curdate(), 30)), ' 00:00:00') and concat(curdate(), ' 23:59:59') order by dateTime desc")
    public Double show30daySellingPrice();
    @ResultType(Double.class)
    @Select("select SUM(price) from refillRecords where dateTime between concat((subdate(curdate(), 30)), ' 00:00:00') and concat(curdate(), ' 23:59:59') order by dateTime desc")
    public Double show30dayRefillingPrice();

    // All Report Data
    @ResultType(Integer.class)
    @Select("select SUM(amount) from sellingRecords")
    public Integer showAllAmount();
    @ResultType(Double.class)
    @Select("select SUM(price) from sellingRecords")
    public Double showAllSellingPrice();
    @ResultType(Double.class)
    @Select("select SUM(price) from refillRecords")
    public Double showAllRefillingPrice();


}
