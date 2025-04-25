package com.example.patterns_banking.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String number;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    private BigDecimal balance;
    private Boolean isActive = true;

    public Account(BuilderAccount builderAccount) {
        this.balance = builderAccount.balance;
        this.number = builderAccount.number;
        this.type = builderAccount.type;
        this.id = builderAccount.id;
    }


    public static BuilderAccount Builder() {
        return new BuilderAccount();
    }


    public static class BuilderAccount {
        private Long id;
        private String number;
        @Enumerated(EnumType.STRING)
        private AccountType type;
        private BigDecimal balance;
        private boolean isActive = true;

        private BuilderAccount() {
        }

        public BuilderAccount id(Long id) {
            this.id = id;
            return this;
        }

        public BuilderAccount number(String number) {
            this.number = number;
            return this;
        }

        public BuilderAccount type(AccountType type) {
            this.type = type;
            return this;
        }

        public BuilderAccount balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public BuilderAccount isActive(boolean bool) {
            this.isActive = bool;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
