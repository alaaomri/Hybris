/**
 *
 */
package de.hybris.merchandise.storefront.updateform;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateProfileForm;


public class CustomUpdateProfileForm extends UpdateProfileForm
{


	private String postalCode;



	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}






}
