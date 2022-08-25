package com.lira17.expensetracker.security;

public record JwtRequest(String username, String password) {
}
