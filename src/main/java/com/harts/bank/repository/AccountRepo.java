package com.harts.bank.repository;

import com.harts.bank.enums.AccountType;
import com.harts.bank.model.SavingsAccount;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountRepo {
    
    @Insert("INSERT INTO account_t (acc_nbr, cif, acc_holder_nm, bank_nm, brnch_nm, ifsc_cd, acc_type, balance, " +
            "is_active, crt_at, crt_by, updt_at, updt_by) VALUES (#{accountNumber}, #{cif}, #{accountHolderName}, #{bankName}, " +
            "#{branchName}, #{ifscCode}, #{accountType}, #{balance}, true, NOW(), #{createdBy}, NOW(), #{updatedBy})")
    int persist(SavingsAccount account);

    @Results(id = "accountResultMap", value = {
        @Result(property = "accountId", column = "acc_id"),
        @Result(property = "accountNumber", column = "acc_nbr"),
        @Result(property = "cif", column = "cif"),
        @Result(property = "accountHolderName", column = "acc_holder_nm"),
        @Result(property = "bankName", column = "bank_nm"),
        @Result(property = "branchName", column = "brnch_nm"),
        @Result(property = "ifscCode", column = "ifsc_cd"),
        @Result(property = "accountType", column = "acc_type"),
        @Result(property = "balance", column = "balance"),
        @Result(property = "active", column = "is_active"),
        @Result(property = "createdAt", column = "crt_at"),
        @Result(property = "createdBy", column = "crt_by"),
        @Result(property = "updatedAt", column = "updt_at"),
        @Result(property = "updatedBy", column = "updt_by")
    })
    @Select("SELECT * FROM account_t WHERE acc_nbr = #{accountNumber}")
    Optional<SavingsAccount> findByAccountNumber(String accountNumber);

    @ResultMap("accountResultMap")
    @Select("SELECT * FROM account_t WHERE cif = #{cif} and acc_type != #{loan}")
    List<SavingsAccount> findByCustomerId(String cif, AccountType loan);

    @ResultMap("accountResultMap")
    @Select("SELECT * FROM account_t WHERE cif = #{customerId} and bank_nm = #{bankName}")
    Optional<SavingsAccount> findByCustomerIdAndBank(String customerId, String bankName);

    @ResultMap("accountResultMap")
    @Select("SELECT * FROM account_t WHERE cif = #{customerId} and bank_nm = #{bankName} and acc_type = #{accountType}")
    Optional<SavingsAccount> findByCustomerIdAndBankAndAccountType(String customerId, String bankName, AccountType accountType);
}
