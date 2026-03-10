package com.harts.bank.repository;

import com.harts.bank.model.Customer;
import com.harts.bank.typehandlers.AddressTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CustomerRepo {

    @Results(id = "customerResultMap", value = {
        @Result(property = "customerId", column = "cif"),
        @Result(property = "firstName", column = "first_name"),
        @Result(property = "lastName", column = "last_name"),
        @Result(property = "email", column = "email"),
        @Result(property = "phoneNumber", column = "phn_nbr"),
        @Result(property = "adhaarNumber", column = "adhr_no"),
        @Result(property = "panNumber", column = "pan_no"),
        @Result(property = "address", column = "address", typeHandler = AddressTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "crt_at"),
        @Result(property = "createdBy", column = "crt_by"),
        @Result(property = "updatedAt", column = "updt_at"),
        @Result(property = "updatedBy", column = "updt_by")
    })
    @Select("SELECT * FROM customer_t WHERE cif = #{customerId}")
     Optional<Customer> findByCif(String customerId);

    @ResultMap("customerResultMap")
     @Select("SELECT * FROM customer_t WHERE adhr_no = #{adhaarNumber}")
    Optional<Customer> findByAdhaarNum(String adhaarNumber);
    
    @ResultMap("customerResultMap")
    @Select("SELECT * FROM customer_t WHERE pan_no = #{panNumber}")
    Optional<Customer> findByPanNum(String panNumber);
    
    @ResultMap("customerResultMap")
        @Select("SELECT * FROM customer_t WHERE first_name = #{firstName} AND last_name = #{lastName}")
    List<Customer> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

     @Insert("INSERT INTO customer_t (cif, bank_nm, first_name, last_name, email, phn_nbr, adhr_no, pan_no, address, is_active, crt_at, crt_by, updt_at, updt_by) " +
             "VALUES (#{customerId}, #{bankName} ,#{firstName}, #{lastName}, #{email}, #{phoneNumber}, #{adhaarNumber}, #{panNumber}, #{address}, #{isActive}, " +
             "#{createdAt}, #{createdBy}, #{updatedAt}, #{updatedBy})")
    int persist(Customer customer);

     @Delete("DELETE FROM customer_t WHERE cif = #{customerId}")
    int delete(String customerId);

     @Update("UPDATE customer_t SET first_name = #{firstName}, last_name = #{lastName}, email = #{email}, phn_nbr = #{phoneNumber}, " +
             "adhr_no = #{adhaarNumber}, pan_no = #{panNumber}, address = #{address}, is_active = #{isActive}, crt_at = #{createdAt}, " +
             "crt_by = #{createdBy}, updt_at = #{updatedAt}, updt_by = #{updatedBy} WHERE cif = #{customerId}")
    void update(Customer customer);

        @ResultMap("customerResultMap")
        @Select("SELECT * FROM customer_t WHERE adhr_no = #{aadharNumber} AND bank_nm = #{bankName}")
    Optional<Customer> findByAdhaarNumWithBank(String aadharNumber, String bankName);
}
