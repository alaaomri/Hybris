/**
 *
 */
package de.hybris.merchandise.storefront.updateform;

import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;


/**
 * @author Alaeddine-OMRI
 *
 */
public class CustomRegisterForm extends RegisterForm
{
	private String postalCode;

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param postalCode
	 *           the postalCode to set
	 */
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

}
