package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final AdminService adminService;

    
}
