package com.clp.kafka.demo.profitability.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfitabilityInputRepository extends JpaRepository<ProfitabilityInput, Long> {
    ProfitabilityInput getByOrderId(Long valueOf);
    void deleteAllByOrderId(Long valueOf);
}
