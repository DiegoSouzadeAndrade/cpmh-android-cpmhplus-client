package br.com.cpmh.plus.sales.orders;

import java.io.Serializable;
import java.util.Date;

public class OrderHistory implements Serializable
{
	private Date date;
	private String stage;
	private String status;

	public OrderHistory()
	{
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getStage()
	{
		return stage;
	}

	public void setStage(String stage)
	{
		this.stage = stage;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
