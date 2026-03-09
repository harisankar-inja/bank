package com.harts.bank.model;

import lombok.Data;

@Data
public class Address {

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

//    @Override
//    public String toString() {
//        return String.join(",",
//            addressLine1 != null ? addressLine1 : "",
//            addressLine2 != null ? addressLine2 : "",
//            city != null ? city : "",
//            state != null ? state : "",
//            postalCode != null ? postalCode : "",
//            country != null ? country : ""
//        );
//    }

    public static Address fromString(String str) {
        String[] parts = str.split(",", -1);
        Address address = new Address();
        address.setAddressLine1(parts.length > 0 ? parts[0] : "");
        address.setAddressLine2(parts.length > 1 ? parts[1] : "");
        address.setCity(parts.length > 2 ? parts[2] : "");
        address.setState(parts.length > 3 ? parts[3] : "");
        address.setPostalCode(parts.length > 4 ? parts[4] : "");
        address.setCountry(parts.length > 5 ? parts[5] : "");
        return address;
    }
}
