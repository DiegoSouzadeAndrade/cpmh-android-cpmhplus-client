package br.com.cpmh.plus.sales.orders;

import java.io.Serializable;

public class OrderDetail implements Serializable
{
	private Long amount;
	private String sku;
	private String productName;
	private String partNumber;
	private String anvisa;
	private String manufacturer;
	private String manufacturerTaxpayerNumber;

	public OrderDetail()
	{
	}

	public Long getAmount()
	{
		return amount;
	}

	public void setAmount(Long amount)
	{
		this.amount = amount;
	}

	public String getAnvisa()
	{
		return anvisa;
	}

	public void setAnvisa(String anvisa)
	{
		this.anvisa = anvisa;
	}

	public String getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public String getManufacturerTaxpayerNumber()
	{
		return manufacturerTaxpayerNumber;
	}

	public void setManufacturerTaxpayerNumber(String manufacturerTaxpayerNumber)
	{
		this.manufacturerTaxpayerNumber = manufacturerTaxpayerNumber;
	}

	public String getPartNumber()
	{
		return partNumber;
	}

	public void setPartNumber(String partNumber)
	{
		this.partNumber = partNumber;
	}

	public String getSku()
	{
		return sku;
	}

	public void setSku(String sku)
	{
		this.sku = sku;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}
}
