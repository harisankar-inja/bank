create table customer_t(
    cif varchar(20) primary key unique,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(100) unique,
    phn_nbr varchar(15),
    adhr_no varchar(16) unique,
    pan_no varchar(10) unique,
    address varchar(200),
    is_active boolean,
    crt_at timestamp,
    crt_by varchar(40),
    updt_at timestamp,
    updt_by varchar(40)
);