create table account_t(
    acc_id serial primary key,
    acc_nbr varchar(20) unique not null,
    cif varchar(20) not null,
    acc_holder_nm varchar(100) not null,
    bank_nm varchar(50) not null,
    brnch_nm varchar(50) not null,
    ifsc_cd varchar(20) not null,
    acc_typ varchar(20) not null,
    balance decimal(15,2),
    is_active boolean,
    crt_at timestamp,
    crt_by varchar(40),
    updt_at timestamp,
    updt_by varchar(40),
    foreign key (cif) references customer_t(cif)
);