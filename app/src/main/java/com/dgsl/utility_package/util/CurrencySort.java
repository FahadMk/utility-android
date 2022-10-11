package com.dgsl.utility_package.util;

import com.dgsl.utility_package.data.model.ConversionRateListModel;

import java.util.Comparator;

public class CurrencySort implements Comparator<ConversionRateListModel> {

    public int compare(ConversionRateListModel one, ConversionRateListModel another) {
        return one.currency_code.compareTo(another.currency_code);
    }
}