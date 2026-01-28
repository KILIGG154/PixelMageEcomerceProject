package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.PurchaseOrderLineRequest;
import com.example.PixelMageEcomerceProject.entity.PurchaseOrder;
import com.example.PixelMageEcomerceProject.entity.PurchaseOrderLine;
import com.example.PixelMageEcomerceProject.repository.PurchaseOrderLineRepository;
import com.example.PixelMageEcomerceProject.repository.PurchaseOrderRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.PurchaseOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    @Autowired
    private PurchaseOrderLineRepository purchaseOrderLineRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public PurchaseOrderLine createPurchaseOrderLine(PurchaseOrderLineRequest purchaseOrderLine, int purchaseOrderId) {
        PurchaseOrder foundingPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
        if (foundingPurchaseOrder != null) {
            PurchaseOrderLine newPurchaseOrderLine = new PurchaseOrderLine();
            newPurchaseOrderLine.setPurchaseOrder(foundingPurchaseOrder);
            newPurchaseOrderLine.setQuantityOrdered(purchaseOrderLine.getQuantityOrdered());
            newPurchaseOrderLine.setQuantityReceived(purchaseOrderLine.getQuantityReceived());
            newPurchaseOrderLine.setQuantityPendingReceived(purchaseOrderLine.getQuantityPendingReceived());
            newPurchaseOrderLine.setUnitPrice(purchaseOrderLine.getUnitPrice());
            newPurchaseOrderLine.setTotalPrice(purchaseOrderLine.getUnitPrice() * purchaseOrderLine.getQuantityOrdered());
            return purchaseOrderLineRepository.save(newPurchaseOrderLine);
        }
        return null;
    }
}
