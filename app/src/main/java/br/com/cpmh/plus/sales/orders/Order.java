package br.com.cpmh.plus.sales.orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.cpmh.plus.sales.Customer;

public class Order implements Serializable
{
	private Long number;
	private String invoice;
	private String category;
	private String type;
	private String stage;
	private String status;

	private Customer customer;

	private String patientName;
	private String doctorName;

	private String salesmanId;
	private Long productManagerId;

	private boolean finished;

	private Date updatedAt;

	private Date registeredAt;

	private List<OrderHistory> history;

	private List<OrderDetail> items;

	public Order()
	{
		items = new ArrayList<>();
		history = new ArrayList<>();
	}

	public Long getNumber()
	{
		return number;
	}

	public void setNumber(Long number)
	{
		this.number = number;
	}

	public String getInvoice()
	{
		return invoice;
	}

	public void setInvoice(String invoice)
	{
		this.invoice = invoice;
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

	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	public String getPatientName()
	{
		return patientName;
	}

	public void setPatientName(String patientName)
	{
		this.patientName = patientName;
	}

	public String getDoctorName()
	{
		return doctorName;
	}

	public void setDoctorName(String doctorName)
	{
		this.doctorName = doctorName;
	}

	public String getSalesmanId()
	{
		return salesmanId;
	}

	public void setSalesmanId(String salesmanId)
	{
		this.salesmanId = salesmanId;
	}

	public Long getProductManagerId()
	{
		return productManagerId;
	}

	public void setProductManagerId(Long productManagerId)
	{
		this.productManagerId = productManagerId;
	}

	public List<OrderHistory> getHistory()
	{
		return history;
	}

	public void setHistory(List<OrderHistory> history)
	{
		this.history = history;
	}

	public List<OrderDetail> getItems()
	{
		return items;
	}

	public void setItems(List<OrderDetail> items)
	{
		this.items = items;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
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
