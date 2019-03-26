/**
 *
 */
package de.hybris.merchandise.dao;

import de.hybris.platform.core.model.product.ProductModel;


/**
 * @author Alaeddine-OMRI
 *
 */
public interface CustomProductDao
{

	// Method returns the number of times that the product was saled
	double findByProduct(ProductModel productModel);
}
