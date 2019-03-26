/**
 *
 */
package de.hybris.merchandise.customer.service;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * @author Alaeddine-OMRI
 *
 */
public interface CustomProductService
{

	// Method returns the number of times that the product was saled
	Double getNumberOfProductSaled(ProductModel product, LanguageModel language);


}
