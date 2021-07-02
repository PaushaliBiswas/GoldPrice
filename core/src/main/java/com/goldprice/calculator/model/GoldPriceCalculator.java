package com.goldprice.calculator.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoldPriceCalculator implements Serializable{

	private static final long serialVersionUID = 1L;
	private double rate;
	private double weight;
	private double price;
	private double discount;

	@Override
	public String toString() {
		String str = "Gold Price (per gram):  " + rate + "\nWeight (in grams):      " + weight;
		if(discount != 0) str += "\nDiscount%:              " + discount;
		str += "\nTotal Price:            " + price;
		return str;
	}
}
