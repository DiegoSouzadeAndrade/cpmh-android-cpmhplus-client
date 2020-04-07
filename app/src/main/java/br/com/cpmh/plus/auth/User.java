package br.com.cpmh.plus.auth;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class User implements Serializable
{
	private String firstName;
	private String middleName;
	private String lastName;
	private Date birthday;

	/**
	 *
	 */
	public User() {}

	/**
	 * @return
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return
	 */
	public String getMiddleName()
	{
		return middleName;
	}

	/**
	 * @param middleName
	 */
	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	/**
	 * @return
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return
	 */
	public Date getBirthday()
	{
		return birthday;
	}

	/**
	 * @param birthday
	 */
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}
}
