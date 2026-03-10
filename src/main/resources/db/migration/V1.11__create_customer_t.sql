create table customer_t(
    cust_id serial primary key,
    cif varchar(20) unique,
    bank_nm varchar(50) not null,
    first_name varchar(50) not null,
    last_name varchar(50),
    email varchar(100) unique not null,
    phn_nbr varchar(15) unique not null,
    adhr_no varchar(16) unique not null,
    pan_no varchar(10) unique not null,
    address varchar(200) not null,
    is_active boolean,
    crt_at timestamp,
    crt_by varchar(40),
    updt_at timestamp,
    updt_by varchar(40)
);