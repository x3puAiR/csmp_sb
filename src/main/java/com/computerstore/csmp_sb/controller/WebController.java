package com.computerstore.csmp_sb.controller;

import com.computerstore.csmp_sb.dao.Dao;
import com.computerstore.csmp_sb.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class WebController {
    // Set Password Screct key
    static final String skey = "BeLS1K1XkQ5A";

    @Autowired
    private Dao dao;
    public static boolean isAuthed = false;

    // Control for Authentication and password check
    @GetMapping("/auth")
    public String showAuth(Model model) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (isAuthed)
            return "redirect:/";
        else {
            model.addAttribute("auth_password");
            return "auth";
        }
    }

    // Auth check success
    @GetMapping("/authSuccess")
    public String authSuccess() {
        if (!isAuthed) return "redirect:/auth";
        else return "redirect:/home";
    }

    // Logout
    @GetMapping("/logout")
    public String logout() {
        isAuthed = false;
        return "redirect:/";
    }

    // Default Requested URI
    @GetMapping("/")
    public String showInit() {
        if (!dao.isInitialized()) {
            return "redirect:/ootb";
        } else {
            if (isAuthed) {
                return "redirect:/home";
            } else return "redirect:/auth";
        }
    }

    // OOTB settings
    @GetMapping("/ootb")
    public String showOOTB() {
        if (dao.isInitialized()) {
            if (isAuthed) {
                return "redirect:/home";
            } else return "redirect:/auth";
        } else return "ootb";
    }

    @GetMapping("/ootb-setup")
    public String showSetup() {
        if (dao.isInitialized()) {
            if (isAuthed) {
                return "redirect:/home";
            } else return "redirect:/auth";
        } else return "ootb-setup";
    }

    // Home Page
    @GetMapping("/home")
    public String showHome() {
        if (!dao.isInitialized()) {
            return "ootb";
        } else {
            if (isAuthed) {
                return "redirect:/inventory";
            } else return "redirect:/auth";
        }
    }

    // Settings
    @GetMapping("/settings")
    public String showSettings() {
        if (!dao.isInitialized()) {
            return "ootb";
        } else {
            if (isAuthed)
                return "settings";
            else return "redirect:/auth";
        }
    }

    // Change password
    @GetMapping("/changePassword")
    public String showChangePass(Model model, @ModelAttribute("auth_password") String oldpass, @ModelAttribute("auth_new_password") String newpass) {
        if (!dao.isInitialized()) {
            return "ootb";
        } else {
            if (isAuthed)
                return "changePass";
            else return "redirect:/auth";
        }
    }

    // Reset
    @GetMapping("/reset")
    public String showReset() {
        if (!dao.isInitialized()) {
            return "ootb";
        } else {
            if (isAuthed)
                return "reset";
            else return "redirect:/auth";
        }
    }

    // Error page
    @RequestMapping("/error1")
    public String showError() {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }
        return "error";
    }

    // Inventory show and functions
    @GetMapping("/inventory")
    public String showInventory(Model model, Part part) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }
        List list = dao.showInventory();
        model.addAttribute("list", list);
        model.addAttribute("part", part);
        return "inventory";
    }

    // Edit price
    @GetMapping("/edit")
    public String showChangePrice(Model model, @ModelAttribute("part") Part part, HttpServletRequest request) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }

        model.addAttribute("price", part.getPrice());
        model.addAttribute("skuId", part.getSkuId());
        model.addAttribute("name", part.getName());
        model.addAttribute("part", part);

        return "edit";
    }

    // Sell
    @GetMapping("/sell")
    public String showSell(Model model, @ModelAttribute("part") Part part, HttpServletRequest request) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }

        model.addAttribute("price", part.getPrice());
        model.addAttribute("skuId", part.getSkuId());
        model.addAttribute("name", part.getName());
        model.addAttribute("part", part);

        return "sell";
    }

    // Refill
    @GetMapping("/refill")
    public String showRefill(Model model, @ModelAttribute("part") Part part, HttpServletRequest request) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }

        model.addAttribute("skuId", part.getSkuId());
        model.addAttribute("name", part.getName());
        model.addAttribute("part", part);

        return "refill";
    }

    // Add inventory items
    @GetMapping("/addItem")
    public String showAddItem(Model model, Part part, Computer computer) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }
        return "additem";
    }

    // Show operation records
    @GetMapping("/records")
    public String showRecords(Model model, sellRecord record) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }

        List records = dao.showRecords();
        if (records.isEmpty()) {
            model.addAttribute("noDataMsg", "No Data.");
            return "record";
        }
        model.addAttribute("records", records);
        model.addAttribute("record", record);

        return "record";
    }

    // Funding Report
    @GetMapping("/report")
    public String showReport(Model model) {
        if (!dao.isInitialized()) {
            return "ootb";
        }
        if (!isAuthed) {
            return "redirect:/auth";
        }

        int past7dayAmount = dao.show7dayAmount();
        double past7daySellingPrice = dao.show7daySellingPrice();
        double past7dayRefillingPrice = dao.show7dayRefillingPrice();
        double past7dayProfit = past7daySellingPrice - past7dayRefillingPrice;
        model.addAttribute("past7dayAmount", past7dayAmount);
        model.addAttribute("past7daySellingPrice", past7daySellingPrice);
        model.addAttribute("past7dayRefillingPrice", past7dayRefillingPrice);
        model.addAttribute("past7dayProfit", past7dayProfit);

        int past30dayAmount = dao.show30dayAmount();
        double past30daySellingPrice = dao.show30daySellingPrice();
        double past30dayRefillingPrice = dao.show30dayRefillingPrice();
        double past30dayProfit = past30daySellingPrice - past30dayRefillingPrice;
        model.addAttribute("past30dayAmount", past30dayAmount);
        model.addAttribute("past30daySellingPrice", past30daySellingPrice);
        model.addAttribute("past30dayRefillingPrice", past30dayRefillingPrice);
        model.addAttribute("past30dayProfit", past30dayProfit);

        int pastAllAmount = dao.showAllAmount();
        double pastAllSellingPrice = dao.showAllSellingPrice();
        double pastAllRefillingPrice = dao.showAllRefillingPrice();
        double pastAllProfit = pastAllSellingPrice - pastAllRefillingPrice;
        model.addAttribute("pastAllAmount", pastAllAmount);
        model.addAttribute("pastAllSellingPrice", pastAllSellingPrice);
        model.addAttribute("pastAllRefillingPrice", pastAllRefillingPrice);
        model.addAttribute("pastAllProfit", pastAllProfit);

        return "report";
    }
}
