/**
 *
 */
package de.hybris.merchandise.customer.service;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;


/**
 * @author Alaeddine-OMRI
 *
 */
public interface CustomCustomerAccountService
{
	public void updateProfile(final CustomerModel customerModel, final String titleCode, final String name, final String login,
			final String postalCode) throws DuplicateUidException;

}
