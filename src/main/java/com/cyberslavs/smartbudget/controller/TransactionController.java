package com.cyberslavs.smartbudget.controller;

import com.cyberslavs.smartbudget.DTO.TransactionDTO;
import com.cyberslavs.smartbudget.model.Transaction;
import com.cyberslavs.smartbudget.model.Wallet;
import com.cyberslavs.smartbudget.repository.CategoryRepository;
import com.cyberslavs.smartbudget.repository.TransactionRepository;
import com.cyberslavs.smartbudget.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping("/wallet/{id}")
    public List<Transaction> findByWallet(@PathVariable Long id){
        if(walletRepository.existsById(id)){
            return transactionRepository.findByWalletId(id);
        }else{
            return new ArrayList<>();
        }

    }
    @GetMapping("/category/{categoryId}")
    public List<Transaction> findByWallet(@PathVariable Long walletId, @PathVariable Long categoryId){
        if(categoryRepository.existsById(categoryId)){
            return transactionRepository.findByCategoryId(categoryId);
        }else{
            return new ArrayList<>();
        }

    }
    @PostMapping("/")
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction){
        Transaction newTransaction=new Transaction(transaction.name, transaction.price, transaction.date);
        walletRepository.findById(transaction.walletId).map(wallet -> {
            categoryRepository.findById(transaction.categoryId).map(category -> {
                newTransaction.setWallet(wallet);
                newTransaction.setCategory(category);
                return transactionRepository.save(newTransaction);
            }).orElseThrow();
            return wallet;
        }).orElseThrow();
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }
}
