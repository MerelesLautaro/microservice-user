package com.lautadev.microservice_user.repository;

import com.lautadev.microservice_user.dto.BenefitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-benefit")
public interface IBenefitAPIClient {
    @GetMapping("/api/benefit/get/{id}")
    public BenefitDTO findBenefit(@PathVariable ("id") Long idBenefit);
}
