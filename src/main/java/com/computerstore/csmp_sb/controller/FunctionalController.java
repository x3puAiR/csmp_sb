package com.computerstore.csmp_sb.controller;

import com.computerstore.csmp_sb.dao.Dao;
import com.computerstore.csmp_sb.entity.*;

import com.computerstore.csmp_sb.security.EncryptPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class FunctionalController {

    @Autowired
    private Dao dao;

    // Set OOTB Status
    void setIsInitialized() {
        dao.setInitialized();
    }

    // Insert and Handle First-use Data
    @PostMapping("/submitOotbData")
    public String submitOotbData(Model model, Part part, Computer computer, HttpServletRequest request, HttpServletResponse response, String pass) {

        for (int i = 1; i <= 30; i++) {

            if (request.getParameter("skuid" + i).equals("")) {
                continue;
            }

            part.setSkuId(Integer.parseInt((request.getParameter("skuid" + i))));
            part.setName(request.getParameter("name" + i));
            part.setAmount(Integer.parseInt(request.getParameter("amount" + i)));
            part.setPrice(Double.parseDouble(request.getParameter("price" + i)));
            System.out.println("No. " + i + ": ID: " + part.getSkuId() + ", Name: " + part.getName() + ", Amount: " + part.getAmount() + ", Price: " + part.getPrice());

            dao.addPart(part);
        }

        for (int i = 31; i <= 60; i++) {

            if (request.getParameter("skuid" + i).equals("")) {
                continue;
            }

            computer.setSkuId(Integer.parseInt((request.getParameter("skuid" + i))));
            computer.setName(request.getParameter("name" + i));
            computer.setAmount(Integer.parseInt(request.getParameter("amount" + i)));
            computer.setPrice(Double.parseDouble(request.getParameter("price" + i)));
            dao.addComputer(computer);
        }

        pass = request.getParameter("confirm_password");
        dao.changePassword(EncryptPass.encrypt(pass, WebController.skey));

        System.out.println("submit ok");
        setIsInitialized();
        return "redirect:/home";
    }

    // Submit Refill
    @PostMapping("/submitRefill")
    public String submitRefill(Model model, @ModelAttribute("part") Part part, HttpServletRequest request) {
        model.addAttribute("skuId", part.getSkuId());
        model.addAttribute("name", part.getName());

        if (part.getAmount() < 0) {
            model.addAttribute("refillMessage", "Invalid input.");
            List list = dao.showInventory();
            model.addAttribute("list", list);
            model.addAttribute("part", part);
            return "inventory";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        dao.refillParts(part.getSkuId(), part.getAmount());
        dao.refillComputers(part.getSkuId(), part.getAmount());
        dao.addRefillRecord(part.getPrice() * part.getAmount(), part.getAmount(), part.getSkuId(), dtf.format(time));
        model.addAttribute("refillMessage", "Amount Changed.");

        List list = dao.showInventory();
        model.addAttribute("list", list);
        model.addAttribute("part", part);
        return "inventory";
    }

    // Submit Add an item
    @PostMapping("/submitAddItem")
    public String submitAddItem(Model model, Part part, Computer computer, refillRecord record, HttpServletRequest request, HttpServletResponse response) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();

        if (request.getParameter("type").equals("part")) {
            part.setSkuId(Integer.parseInt(request.getParameter("skuId")));
            part.setAmount(Integer.parseInt(request.getParameter("amount")));
            part.setPrice(Double.parseDouble(request.getParameter("priceOut")));
            part.setName(request.getParameter("name"));
            dao.addPart(part);
            dao.addRefillRecord(Double.parseDouble(request.getParameter("priceIn")) * Double.parseDouble(request.getParameter("amount")), Integer.parseInt(request.getParameter("amount")), Integer.parseInt(request.getParameter("skuId")), dtf.format(time));

            List list = dao.showInventory();
            model.addAttribute("list", list);
            return "inventory";
        } else {
            computer.setSkuId(Integer.parseInt(request.getParameter("skuId")));
            computer.setAmount(Integer.parseInt(request.getParameter("amount")));
            computer.setPrice(Double.parseDouble(request.getParameter("priceOut")));
            computer.setName(request.getParameter("name"));
            dao.addComputer(computer);
            dao.addRefillRecord(Double.parseDouble(request.getParameter("priceIn")) * Double.parseDouble(request.getParameter("amount")), Integer.parseInt(request.getParameter("amount")), Integer.parseInt(request.getParameter("skuId")), dtf.format(time));

            List list = dao.showInventory();
            model.addAttribute("list", list);
            return "inventory";
        }
    }

    // Price Change
    @PostMapping("/submitPriceChange")
    public String submitChangePrice(Model model, @ModelAttribute("part") Part part, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("price", part.getPrice());
        model.addAttribute("skuId", part.getSkuId());

        if (part.getPrice() <= 0) {
            model.addAttribute("changePriceMessage", "Invalid input.");
            List list = dao.showInventory();
            model.addAttribute("list", list);
            model.addAttribute("part", part);
            return "inventory";
        }

        dao.updatePartPrice(part.getSkuId(), part.getPrice());
        dao.updateComputerPrice(part.getSkuId(), part.getPrice());
        model.addAttribute("changePriceMessage", "Price Changed.");

        List list = dao.showInventory();
        model.addAttribute("list", list);
        model.addAttribute("part", part);
        return "inventory";
    }

    // Sell
    @PostMapping("/submitSell")
    public String submitSell(Model model, @ModelAttribute("part") Part part, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("price", part.getPrice());
        model.addAttribute("skuId", part.getSkuId());
        model.addAttribute("amount", part.getAmount());

        if (part.getAmount() <= 0) {
            model.addAttribute("sellMessage", "Invalid input.");
            List list = dao.showInventory();
            model.addAttribute("list", list);
            model.addAttribute("part", part);
            return "inventory";
        } else if (dao.checkAvailable(part.getSkuId()) < part.getAmount()) {
            model.addAttribute("sellMessage", "Item inventory shortage.");
            List list = dao.showInventory();
            model.addAttribute("list", list);
            model.addAttribute("part", part);
            return "inventory";
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            dao.sellPart(part.getAmount(), part.getSkuId());
            dao.sellComputer(part.getAmount(), part.getSkuId());
            dao.addSellRecord(part.getPrice() * part.getAmount(), part.getAmount(), part.getSkuId(), dtf.format(time));
            model.addAttribute("sellMessage", "Sold.");

            List list = dao.showInventory();
            model.addAttribute("list", list);
            model.addAttribute("part", part);
            return "inventory";
        }
    }

    // Change Password
    @PostMapping("/submitChangePassword")
    public String changePassword(Model model, @ModelAttribute("auth_password") String oldPass, @ModelAttribute("auth_new_password") String newPass) {
        if (!dao.isInitialized()) {
            return "ootb";
        } else {

            model.addAttribute("auth_password", oldPass);
            model.addAttribute("auth_new_password", newPass);

            oldPass = EncryptPass.encrypt(oldPass, WebController.skey);
            newPass = EncryptPass.encrypt(newPass, WebController.skey);
            if (dao.checkAuth(oldPass) == null) {
                model.addAttribute("message", "Old Password Wrong.");
                return "changePass";
            } else {
                dao.changePassword(newPass);
                model.addAttribute("changePasswordMessage", "Password changed!");
                List list = dao.showInventory();
                model.addAttribute("list", list);
                return "inventory";
            }
        }
    }

    // Check Password Authentication
    @PostMapping("/checkAuth")
    public String checkAuth(@ModelAttribute("auth_password") String pass, Model model) {
        pass = EncryptPass.encrypt(pass, WebController.skey);
        if (dao.checkAuth(pass) == null) {
            model.addAttribute("message", "Validation Failed.");
            return "auth";
        } else {
            WebController.isAuthed = true;
            return "redirect:/authSuccess";
        }
    }

    // Reset System
    @GetMapping("/submitReset")
    public String submitReset() {
        if (!dao.isInitialized()) {
            return "ootb";
        } else {
            if (WebController.isAuthed) {
                dao.reset();
                return "redirect:/";
            } else return "redirect:/auth";
        }
    }
}
