package kroryi.demo.controller;

import kroryi.demo.domain.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import kroryi.demo.Service.ProductService;
import kroryi.demo.domain.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class ProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/products")
    public String products(Model model) {

        List<Product> products = productService.findAll();
        log.info("제품목록 : {}", products);
        model.addAttribute("products", products);

        Category category = new Category();
        category.setId(3L);
//        category.setName("생활용품");
        List<Product> products_category = productService.getProductsByCategory(category);
        log.info("제품 카테고리 목록 : {}", products_category);

        for(Product product : products_category) {
            log.info("----->{}", product.getName());
        }
        model.addAttribute("products_category", products_category);

        List<Product> product_price = productService.getProductsPriceGreaterThan(BigDecimal.valueOf(2000));

        for(Product product : product_price) {
            log.info("-----1>{}", product.getName());
        }

        model.addAttribute("product_price", product_price);

        return "/products/product_list";
    }
}
