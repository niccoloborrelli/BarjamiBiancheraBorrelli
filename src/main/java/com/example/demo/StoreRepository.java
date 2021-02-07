package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StoreRepository extends CrudRepository<Store, Integer> {
    Store findByChainAndAddress(String chain, String address);
    Store findById(int id);
    List<Store> findAll();
}
