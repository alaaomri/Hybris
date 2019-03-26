/**
 *
 */
package de.hybris.merchandise.custom.service.impl;

import de.hybris.merchandise.customer.service.CustomProductService;
import de.hybris.merchandise.dao.impl.CustomDefaultProductDao;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * @author Alaeddine-OMRI
 *
 */
public class CustomDefaultProductService implements CustomProductService
{
	CustomDefaultProductDao customDefaultProductDao;

	// Method returns the number of times that the product was sold
	@Override
	public Double getNumberOfProductSaled(final ProductModel product, final LanguageModel language)
	{
		final double findByProduct = customDefaultProductDao.findByProduct(product);
		if (findByProduct > 0)
		{
			return new Double(findByProduct);
		}
		else
		{
			return null;
		}
	}




	/**
	 * @return the customDefaultProductDao
	 */
	public CustomDefaultProductDao getCustomDefaultProductDao()
	{
		return customDefaultProductDao;
	}

	/**
	 * @param customDefaultProductDao
	 *           the customDefaultProductDao to set
	 */
	public void setCustomDefaultProductDao(final CustomDefaultProductDao customDefaultProductDao)
	{
		this.customDefaultProductDao = customDefaultProductDao;
	}






}
