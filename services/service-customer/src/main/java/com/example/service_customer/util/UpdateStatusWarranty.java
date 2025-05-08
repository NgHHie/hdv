package com.example.service_customer.util;

import com.example.service_customer.constant.WarrantyStatus;

public class UpdateStatusWarranty {

    public static boolean updateStatus(String oldStatus, String newStatus) {
        try{
            WarrantyStatus oldS = WarrantyStatus.valueOf(oldStatus);
            WarrantyStatus newS = WarrantyStatus.valueOf(newStatus);
            
            int curIndex = oldS.ordinal();
            int newIndex = newS.ordinal();
    
            if(newIndex == curIndex + 1) return true;
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
        
    }
    
}
