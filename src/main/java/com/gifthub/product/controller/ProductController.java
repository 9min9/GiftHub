package com.gifthub.product.controller;

import com.gifthub.gifticon.dto.storage.GifticonStorageDto;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.enumeration.CategoryName;
import com.gifthub.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    private final String path = "C:/OcrPractice/";

    @GetMapping("/add/excel")
    public void AddProductFromExcel() {
        List<ProductDto> productList = new ArrayList<>();
        String ExcelName = "기프티쇼 비즈_상품리스트_비즈_API.xlsx";
        try {
            FileInputStream file = new FileInputStream(path + ExcelName);
            Workbook workbook = null;


            if (ExcelName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file);
            } else if (ExcelName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file);
            } else {
                throw new RuntimeException("file type not matched");
            }


            if (workbook == null) {
                throw new RuntimeException("no data in file");
            }

            int rowindx = 0;
            int columnindex = 0;

            Sheet worksheet = workbook.getSheetAt(0);

            int rows = worksheet.getPhysicalNumberOfRows();
            for (rowindx = 13; rowindx < rows; rowindx++) { // 13행부터 한 줄씩 읽는다
                Row row = worksheet.getRow(rowindx);
                if (row != null) { // 행이 비어있지 않다면
                    ProductDto productDto = new ProductDto();
//                    productDto.setId((long) row.getCell(0).getNumericCellValue());  // id
                    productDto.setBrandName(row.getCell(1).getStringCellValue());   // 브랜드명
                    productDto.setName(row.getCell(2).getStringCellValue());        // 상품명
                    productDto.setPrice((long) row.getCell(4).getNumericCellValue());   // 판매가격
                    productDto.setCategory(row.getCell(5).getStringCellValue());        // 카테고리

                    productList.add(productDto);

                }

            }
            Long countLine = productService.saveAll(productList);
            System.out.println("총 라인수 :" + countLine);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/get/category")
    public ResponseEntity<Object> getCategoryList(@RequestHeader HttpHeaders headers) {
        List<String> categoryList = productService.getAllCategory();
        HashMap<Object, Object> result = new HashMap<>();

        for (String kor : categoryList) {
            CategoryName eng = CategoryName.ofKor(kor);
            result.put(eng, kor);
        }

        if (categoryList.isEmpty() || categoryList == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/test")
    public void testProductDto() {
        List<ProductDto> productDtoList = productService.getAllProduct();
        for (ProductDto productDto : productDtoList) {
            System.out.println(productDto.getName());
            System.out.println(productDto.getBrandName());
            System.out.println(productDto.getPrice());
            System.out.println(productDto.getId());
            System.out.println(productDto.getCategory());
        }
    }

    // TODO : 구매페이지의 해당 product 클릭시 해당 product_id를 갖는 GifticonList를 가져오기
//    @GetMapping("/get/gifticon")
//    public void getGifticonByProduct(@Param("productId") Long productId) {
//        List<GifticonDto> gifticonDtoList = gifticonService.getGifticonByProduct(productId);
//        //
//    }

    // TODO : 금액별, 남은 유효기간별(임박순) 정렬하기
    @GetMapping("/{category}/brands")
    public ResponseEntity<Object> getBrand(@PathVariable("category") String category) {
        boolean isEng = category.matches("[a-zA-Z]*");

        CategoryName categoryName = null;
        if (isEng) {
            categoryName = CategoryName.ofEng(category);
        } else {
            categoryName = CategoryName.ofKor(category);
        }

        List<String> gifticonBrandName = productService.getBrandName(categoryName);

        return ResponseEntity.ok(gifticonBrandName);
    }

    @GetMapping("/brands/{brand}")
    public ResponseEntity<Object> getProductByBrand(@PathVariable("brand") String brand) {
        List<ProductDto> brands = productService.getProductByBrand(brand);

        return ResponseEntity.ok(brands);
    }

    @GetMapping("/products")
    public ResponseEntity<Object> getAllProducts() {
        List<ProductDto> products = productService.getAllProduct();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Object> getAllProductsByBrands(@PathVariable("category") String category) {
        String cat = category.replaceAll("-", "/");

        List<ProductDto> products = productService.getProductByCategory(cat);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/page/{category}/{brand}")
    public ResponseEntity<Object> getProductByCategoryAndBrand(Pageable pageable,
                                                               @PathVariable("category") String category,
                                                               @PathVariable("brand") String brand) {
        String cat = category.replaceAll("-", "/");

        Page<ProductDto> products = null;
        if (category.equals("전체")) {
            products = productService.getAllProduct(pageable);
        } else if (brand.equals("전체")) {
            products = productService.getProductByCategory(pageable, cat);
        } else {
            products = productService.getProductByBrand(pageable, brand);
        }


        return ResponseEntity.ok(products);
    }

    @GetMapping("/page/search/{category}/{brand}/{name}")
    public ResponseEntity<Object> getProductByCategoryAndBrand(Pageable pageable,
                                                               @PathVariable("category") String category,
                                                               @PathVariable("brand") String brand,
                                                               @PathVariable("name") String name) {
        String cat = category.replaceAll("-", "/");

        Page<ProductDto> products = null;
        if (category.equals("전체")) {
            products = productService.getAllProductByName(pageable, name);
        } else if (brand.equals("전체")) {
            products = productService.getProductByCategoryByName(pageable, cat, name);
        } else {
            products = productService.getProductByBrandByName(pageable, cat, brand, name);
        }


        return ResponseEntity.ok(products);
    }

}
