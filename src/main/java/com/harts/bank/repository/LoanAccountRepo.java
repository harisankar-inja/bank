package com.harts.bank.repository;

import com.harts.bank.model.LoanAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoanAccountRepo {

    @Insert("INSERT INTO loan_account_t (cif, ln_acc_nbr, ln_acc_holder_nm, acc_typ, sub_acc_typ, ln_amt, emi_amt, emi_due_dt, ln_trm_yrs, int_rate, crdt_score, ann_inc, exist_emis, emp_typ, is_active, ln_strt_dt, ln_end_dt, crt_at, updt_at, crt_by, updt_by) " +
            "VALUES (#{cif}, #{loanAccountNumber}, #{accountHolderName}, #{accountType}, #{subAccountType}, #{loanAmount}, #{emiAmount}, #{emiDueDate}, #{loanTermInYears}, #{interestRate}, #{creditScore}, #{annualIncome}, #{existingEmis}, #{employmentType}, #{active}, #{loanStartDate}, #{loanEndDate}, NOW(), NOW(), #{createdBy}, #{updatedBy})")
    int persist(LoanAccount account);


}
