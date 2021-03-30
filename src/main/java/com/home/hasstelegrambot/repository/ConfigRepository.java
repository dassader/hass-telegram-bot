package com.home.hasstelegrambot.repository;

import com.home.hasstelegrambot.repository.entity.config.BudgetConfigEntity;
import com.home.hasstelegrambot.repository.entity.config.ConfigEntity;
import com.home.hasstelegrambot.repository.entity.config.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, ConfigType> {

    @Query("SELECT config FROM BudgetConfigEntity config WHERE config.type = 'BUDGET'")
    BudgetConfigEntity getBudgetConfig();
}
