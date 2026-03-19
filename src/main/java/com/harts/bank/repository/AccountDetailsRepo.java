package com.harts.bank.repository;

import com.harts.bank.api.request.dto.Account;
import com.harts.bank.api.response.CustomerResponse;
import com.harts.bank.enums.AccountType;
import com.harts.bank.model.CustomerDetails;
import com.harts.bank.model.SavingsAccount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountDetailsRepo {

    @Results(id = "customerDetailsResultMap", value = {
            @Result(property = "customerId", column = "cif"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "adhaarNumber", column = "adhr_no"),
            @Result(property = "panNumber", column = "pan_no"),
            @Result(property = "email", column = "email"),
            @Result(property = "phoneNumber", column = "phn_nbr"),
            @Result(property = "bankName", column = "bank_nm"),
            @Result(property = "accounts", javaType = List.class, column = "cif",
                    many = @Many(select = "getAllAccountsByCif")),
            @Result(property = "loanAccounts", javaType = List.class, column = "cif",
                    many = @Many(select = "getAllLoanAccountsByCif"))
    })
    @Select("SELECT c.cif, c.first_name, c.last_name, c.adhr_no, c.pan_no, " +
            "c.email, c.phn_nbr, c.bank_nm " +
            "FROM customer_t c " +
            "WHERE c.adhr_no = #{aadharNumber}")
    List<CustomerDetails> getAccountsByAadharNumber(String aadharNumber);

    @Select("SELECT acc_nbr AS accountNumber, acc_typ AS accountType, NULL AS loanType FROM account_t WHERE cif = #{cif}")
    List<Account> getAllAccountsByCif(String cif);

    @Select("SELECT ln_acc_nbr AS accountNumber, acc_typ AS accountType, ln_typ AS loanType FROM loan_account_t WHERE cif = #{cif}")
    List<Account> getAllLoanAccountsByCif(String cif);

    @ResultMap("customerDetailsResultMap")
    @Select("SELECT c.cif, c.first_name, c.last_name, c.adhr_no, c.pan_no, " +
            "c.email, c.phn_nbr, c.bank_nm " +
            "FROM customer_t c " +
            "WHERE c.pan_no = #{panNumber}")
    List<CustomerDetails> getLoanAccountsByPanNumber(String panNumber);
}
