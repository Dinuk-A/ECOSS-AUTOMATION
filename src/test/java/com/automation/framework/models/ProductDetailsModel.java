package com.automation.framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductDetailsModel {
    @JsonProperty("SKU")
    private int sku;

    @JsonProperty("Category")
    private String category;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Price")
    private int price;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Color")
    private String color;

    @JsonProperty("Indication")
    private String indication;

    @JsonProperty("AlloyType")
    private int alloyType;

    @JsonProperty("PointTwoPercentYieldStrengthInMPaCured")
    private int pointTwoPercentYieldStrengthInMPaCured;

    @JsonProperty("VickersHardness")
    private int vickersHardness;

    @JsonProperty("ElongationAtBreakInPercent")
    private int elongationAtBreakInPercent;

    @JsonProperty("MeltingIntervalCelsius")
    private String meltingIntervalCelsius;

    @JsonProperty("ElementMnInPercent")
    private String elementMnInPercent;

    @JsonProperty("ElementFeInPercent")
    private String elementFeInPercent;

    @JsonProperty("ElementCInPercent")
    private String elementCInPercent;

    @JsonProperty("ElementCoInPercent")
    private String elementCoInPercent;

    @JsonProperty("ElementCrInPercent")
    private String elementCrInPercent;

    @JsonProperty("ElementMoInPercent")
    private String elementMoInPercent;

    @JsonProperty("ElementSiInPercent")
    private String elementSiInPercent;

    @JsonProperty("ElementWInPercent")
    private String elementWInPercent;

    @JsonProperty("PriceEURInCent")
    private int priceEURInCent;
}
