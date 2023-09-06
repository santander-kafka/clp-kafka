package com.clp.kafka.demo.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<BaseProductData, Long> {

}
