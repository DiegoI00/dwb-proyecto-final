package com.invoice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.invoice.api.entity.Invoice;

public interface RepoInvoice extends JpaRepository<Invoice, String> {

    @Query("SELECT i FROM Invoice i WHERE i.user_id = :user_id")
    List<Invoice> findAllByUserId(@Param("user_id") Integer user_id);
}