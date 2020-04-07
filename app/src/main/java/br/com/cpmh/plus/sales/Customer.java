package br.com.cpmh.plus.sales;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class Customer implements Serializable
{
	private String name;
	private String companyName;
	private String category;
	private String type;
	private String taxpayerNumber;
	private Boolean active;
	private Date updatedAt;
	private Date registeredAt;

	public Customer() { }

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTaxpayerNumber()
	{
		return taxpayerNumber;
	}

	public void setTaxpayerNumber(String taxpayerNumber)
	{
		this.taxpayerNumber = taxpayerNumber;
	}

	public Boolean getActive()
	{
		return active;
	}

	public void setActive(Boolean active)
	{
		this.active = active;
	}

	public Date getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public Date getRegisteredAt()
	{
		return registeredAt;
	}

	public void setRegisteredAt(Date registeredAt)
	{
		this.registeredAt = registeredAt;
	}
}
