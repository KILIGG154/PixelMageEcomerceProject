package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.PurchaseOrderLineRequest;
import com.example.PixelMageEcomerceProject.entity.PurchaseOrderLine;
import org.springframework.stereotype.Service;


@Service
public interface PurchaseOrderLineService {
    PurchaseOrderLine createPurchaseOrderLine(PurchaseOrderLineRequest purchaseOrderLine, int purchaseOrderId);
}
