package com.journalapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.journalapp.model.OtpVerification;

public interface OtpJPARepository extends JpaRepository<OtpVerification, String> {

}
