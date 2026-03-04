package com.inventory.service;

import com.inventory.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryLogService {

    public void logInventoryChange(Product product, String changeType, int quantityChange,
                                    int quantityBefore, Long referenceId) {
        // In a full implementation, persist to inventory_logs table
        log.info("INVENTORY LOG: Product={} Type={} Change={} Before={} After={}",
            product.getName(), changeType, quantityChange, quantityBefore,
            quantityBefore + (changeType.equals("SALE") ? -quantityChange : quantityChange));
    }
}
