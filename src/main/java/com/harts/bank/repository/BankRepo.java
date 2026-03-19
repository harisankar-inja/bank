package com.harts.bank.repository;

import com.harts.bank.api.response.CustomerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BankRepo {


    @Results(value = {
            @Result(property = "customerId", column = "cif"),
            @Result(property = "bankName", column = "bank_nm"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name")
    })
    @Select("SELECT cif, bank_nm, first_name, last_name FROM customer_t WHERE bank_nm = #{bankName}")
    List<CustomerResponse> getCustomersByBankName(String bankName);
}
