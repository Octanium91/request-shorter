package com.requestshorter.frontapi.model.statistic

class StatisticResponseDto {

    Map<String, Integer> country

    Map<String, Map<String, Integer>> city

    List<StatisticData> os

}
