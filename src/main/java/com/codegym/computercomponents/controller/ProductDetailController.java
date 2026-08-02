package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.*;
import com.codegym.computercomponents.service.IProductImageService;
import com.codegym.computercomponents.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductDetailController {

    private final IProductService productService;
    private final IProductImageService productImageService;

    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        List<ProductImage> images = productImageService.getImagesByProductId(id);

        Map<String, String> specs = new LinkedHashMap<>();

        if (product instanceof Cpu) {
            Cpu cpu = (Cpu) product;
            if (cpu.getSocket() != null) specs.put("Socket", cpu.getSocket());
            if (cpu.getSeries() != null) specs.put("Dòng (Series)", cpu.getSeries());
            if (cpu.getSegment() != null) specs.put("Phân khúc", cpu.getSegment());
            if (cpu.getModelNumber() != null) specs.put("Mã CPU", cpu.getModelNumber());
            if (cpu.getSuffix() != null) specs.put("Hậu tố", cpu.getSuffix());
            if (cpu.getCoreCount() != null) specs.put("Số nhân", cpu.getCoreCount() + " Nhân");
            if (cpu.getThreadCount() != null) specs.put("Số luồng", cpu.getThreadCount() + " Luồng");
            if (cpu.getBaseClockGHz() != null) specs.put("Xung nhịp cơ bản", cpu.getBaseClockGHz() + " GHz");
        } else if (product instanceof Vga) {
            Vga vga = (Vga) product;
            if (vga.getGpuBrand() != null) specs.put("Hãng GPU", vga.getGpuBrand());
            if (vga.getGpuModel() != null) specs.put("Model GPU", vga.getGpuModel());
            if (vga.getVram() != null) specs.put("Dung lượng VRAM", vga.getVram() + " GB");
            if (vga.getMemoryType() != null) specs.put("Loại bộ nhớ", vga.getMemoryType());
            if (vga.getSeries() != null) specs.put("Dòng (Series)", vga.getSeries());
        } else if (product instanceof Ram) {
            Ram ram = (Ram) product;
            if (ram.getCapacity() != null) specs.put("Dung lượng", ram.getCapacity() + " GB");
            if (ram.getRamType() != null) specs.put("Loại RAM", ram.getRamType());
            if (ram.getBusSpeed() != null) specs.put("Tốc độ (Bus)", ram.getBusSpeed() + " MHz");
        } else if (product instanceof Mainboard) {
            Mainboard mb = (Mainboard) product;
            if (mb.getModel() != null) specs.put("Model", mb.getModel());
            if (mb.getSocket() != null) specs.put("Socket", mb.getSocket());
            if (mb.getChipset() != null) specs.put("Chipset", mb.getChipset());
            if (mb.getRamType() != null) specs.put("Loại RAM hỗ trợ", mb.getRamType());
            if (mb.getFormFactor() != null) specs.put("Kích thước (Form Factor)", mb.getFormFactor());
        } else if (product instanceof Storage) {
            Storage storage = (Storage) product;
            if (storage.getType() != null) specs.put("Loại ổ cứng", storage.getType());
            if (storage.getCapacity() != null && storage.getCapacityUnit() != null) specs.put("Dung lượng", storage.getCapacity() + " " + storage.getCapacityUnit());
            if (storage.getConnectionType() != null) specs.put("Chuẩn kết nối", storage.getConnectionType());
            if (storage.getPcieGen() != null) specs.put("Chuẩn PCIe", storage.getPcieGen());
            if (storage.getReadSpeed() != null) specs.put("Tốc độ đọc", storage.getReadSpeed() + " MB/s");
            if (storage.getWriteSpeed() != null) specs.put("Tốc độ ghi", storage.getWriteSpeed() + " MB/s");
            if (storage.getRpm() != null) specs.put("Tốc độ vòng quay", storage.getRpm() + " RPM");
            if (storage.getCache() != null && storage.getCacheUnit() != null) specs.put("Bộ nhớ đệm", storage.getCache() + " " + storage.getCacheUnit());
        } else if (product instanceof Psu) {
            Psu psu = (Psu) product;
            if (psu.getWattage() != null) specs.put("Công suất định mức", psu.getWattage() + " W");
            if (psu.getEfficiency() != null) specs.put("Chuẩn hiệu suất", psu.getEfficiency());
            if (psu.getFormFactor() != null) specs.put("Kích thước (Form Factor)", psu.getFormFactor());
        } else if (product instanceof CasePc) {
            CasePc casePc = (CasePc) product;
            if (casePc.getModel() != null) specs.put("Model", casePc.getModel());
            if (casePc.getFormFactor() != null) specs.put("Kích thước case", casePc.getFormFactor());
            if (casePc.getMotherboardSupport() != null) specs.put("Mainboard hỗ trợ", casePc.getMotherboardSupport());
        }

        model.addAttribute("product", product);
        model.addAttribute("images", images);
        model.addAttribute("specs", specs);
        
        return "product-detail";
    }
}
