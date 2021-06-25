package com.requestshorter.frontapi.model.statistic

class StatisticResponseDto {

    Map<String, Integer> country

    Map<String, Map<String, Integer>> city

    Map<String, Integer> mobile

    Map<String, Integer> browser

    Map<String, Integer> device

    Map<String, Integer> os

}
