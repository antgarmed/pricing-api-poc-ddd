package com.example.prices.prices.application.commands;

import java.time.LocalDateTime;

public record GetPriceCommand(long brandId, long productId, LocalDateTime date) {

}
