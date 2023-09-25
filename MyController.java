package vps9d5e18f7.vps.ovh.net.studi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {

    @GetMapping("/admin/product.html")
    public ModelAndView adminProductHtml() {
        ModelAndView modelAndView = new ModelAndView("admin/product");
        return modelAndView;
    }
    
    @GetMapping("/admin/promotion.html")
    public ModelAndView adminPromotionHtml() {
        ModelAndView modelAndView = new ModelAndView("admin/promotion");
        return modelAndView;
    }
    
    @GetMapping("/catalog.html")
    public ModelAndView adminCatalogHtml() {
        ModelAndView modelAndView = new ModelAndView("catalog");
        return modelAndView;
    }
}