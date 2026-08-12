package com.rvmchua.bank.util;

public class AccountNumberMaker {
    private AccountNumberMaker() {}

    public static int randomAccountNumber() {
        return (int) (Math.random() * 1_000_000) + 1_000_000;
    }
}
