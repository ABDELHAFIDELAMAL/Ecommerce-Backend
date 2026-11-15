package com.example.demo.services;

import com.example.demo.entities.Promotion;
import com.example.demo.enums.DiscountType;
import com.example.demo.exceptions.AlReadyExistException;
import com.example.demo.exceptions.NotFoundException;

import java.util.List;

public interface IPromotionService {
    List<Promotion> getAllPromotions();
    Promotion getPromotionById(Long promotionId) throws NotFoundException;
    Promotion createPromotion(Promotion promotion) throws AlReadyExistException;
    void deletePromotion(Long promotionId) throws NotFoundException;
    Promotion updatePromotion(Long promotionId , Promotion promotion) throws NotFoundException, AlReadyExistException;
    List<Promotion> getPromotionsActive();
    List<Promotion> getPromotionsByDiscountType(DiscountType discountType) ;
}
