package com.home.hasstelegrambot.repository;

import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import com.home.hasstelegrambot.repository.entity.NumberStatisticType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface NumberStatisticRepository extends CrudRepository<NumberStatisticEntity, Integer> {
    Page<NumberStatisticEntity> findAllByType(NumberStatisticType type, Pageable pageable);
}
