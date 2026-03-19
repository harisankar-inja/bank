package com.harts.bank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "bank-branches")
@Data
public class BankBranchesConfig {
    private List<Bank> banks;

    @Data
    public static class Bank {
        private String name;
        private List<Branch> branches;
    }

    @Data
    public static class Branch {
        private String name;
        private String ifsc;
    }
}
