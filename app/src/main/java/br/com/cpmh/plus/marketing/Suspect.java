package br.com.cpmh.plus.marketing;

import android.support.v4.view.PagerTitleStrip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Suspect implements Serializable
{
	private String name;
	private String type;
	private List<String> interestList;
	private String email;
	private String phone;
	private String city;
	private String federativeUnit;
	private Date birthday;
	private Date updatedAt;
	private Date registeredAt;

	public Suspect()
	{
		interestList = new ArrayList<>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	public List<String> getInterestList()
	{
		return interestList;
	}

	public void setInterestList(List<String> interestList)
	{
		this.interestList = interestList;
	}

	public void addInterest(String interest)
	{
		interestList.add(interest);
	}

	public void removeInterest(String interest)
	{
		interestList.remove(interest);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFederativeUnit() {
		return federativeUnit;
	}

	public void setFederativeUnit(String federativeUnit) {
		this.federativeUnit = federativeUnit;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Date registeredAt) {
		this.registeredAt = registeredAt;
	}

}
