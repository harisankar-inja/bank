package com.harts.bank.repository;

import com.harts.bank.api.response.LoanAccountResponse;
import com.harts.bank.enums.AccountType;
import com.harts.bank.model.LoanAccount;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LoanAccountRepo {

    @Insert("INSERT INTO loan_account_t (cif, bank_nm, pan_nbr, ln_acc_nbr, ln_acc_holder_nm, acc_typ, ln_typ, linked_savings_acc_nbr, ln_amt, emi_amt, pend_emis, emi_due_dt, ln_trm_yrs, int_rate, crdt_score, ann_inc, exist_emis, emp_typ, is_active, ln_strt_dt, ln_end_dt, crt_at, updt_at, crt_by, updt_by) " +
            "VALUES (#{cif}, #{bankName}, #{panNumber}, #{loanAccountNumber}, #{accountHolderName}, #{accountType}, #{loanType}, #{linkedSavingsAccountNumber}, #{loanAmount}, #{emiAmount}, #{pendingEmis}, #{emiDueDate}, #{loanTermInYears}, #{interestRate}, #{creditScore}, #{annualIncome}, #{existingEmis}, #{employmentType}, #{active}, #{loanStartDate}, #{loanEndDate}, NOW(), NOW(), #{createdBy}, #{updatedBy})")
    int persist(LoanAccount account);


    @Results(id = "loanAccountResultMap", value = {
            @Result(property = "accountHolderName", column = "ln_acc_holder_nm"),
            @Result(property = "cif", column = "cif"),
            @Result(property = "panNumber", column = "pan_nbr"),
            @Result(property = "loanAccountNumber", column = "ln_acc_nbr"),
            @Result(property = "linkedSavingsAccountNumber", column = "linked_savings_acc_nbr"),
            @Result(property = "accountType", column = "acc_typ"),
            @Result(property = "loanType", column = "ln_typ"),
            @Result(property = "loanAmount", column = "ln_amt"),
            @Result(property = "emiAmount", column = "emi_amt"),
            @Result(property = "pendingEmis", column = "pend_emis"),
            @Result(property = "emiDueDate", column = "emi_due_dt"),
            @Result(property = "loanTermInYears", column = "ln_trm_yrs"),
            @Result(property = "interestRate", column = "int_rate"),
            @Result(property = "creditScore", column = "crdt_score"),
            @Result(property = "annualIncome", column = "ann_inc"),
            @Result(property = "existingEmis", column = "exist_emis"),
            @Result(property = "employmentType", column = "emp_typ"),
            @Result(property = "active", column = "is_active"),
            @Result(property = "loanStartDate", column = "ln_strt_dt"),
            @Result(property = "loanEndDate", column = "ln_end_dt")
    })
    @Select("SELECT * FROM loan_account_t WHERE pan_nbr = #{panNumber} AND is_active = true")
    List<LoanAccount> getLoanAccountsByPanNum(@NotBlank String panNumber);
    //TODO: change return type to LoanAccountResponse everywhere
    @ResultMap("loanAccountResultMap")
    @Select("SELECT * FROM loan_account_t WHERE bank_nm = #{bankName} AND is_active = true")
    List<LoanAccount> findAllAccounts(String bankName);

    @ResultMap("loanAccountResultMap")
    @Select("SELECT * FROM loan_account_t WHERE cif = #{cif} AND is_active = true")
    List<LoanAccount> getAccountsByCID(String cif);

    @ResultMap("loanAccountResultMap")
    @Select("SELECT * FROM loan_account_t WHERE ln_acc_nbr = #{accountNumber} AND is_active = true")
    Optional<LoanAccount> findByAccountNumber(String accountNumber);
}
