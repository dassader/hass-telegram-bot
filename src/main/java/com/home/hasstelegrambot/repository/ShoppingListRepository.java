package com.home.hasstelegrambot.repository;

import com.home.hasstelegrambot.repository.entity.ShoppingListEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingListEntity, Integer> {
    Iterable<ShoppingListEntity> findAllByCheckedIsTrue();
    Iterable<ShoppingListEntity> findAllByCheckedIsFalse();
    void deleteByCheckedIsTrue();
}
