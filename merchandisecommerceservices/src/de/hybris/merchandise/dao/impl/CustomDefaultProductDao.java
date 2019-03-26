/**
 *
 */
package de.hybris.merchandise.dao.impl;


import de.hybris.merchandise.dao.CustomProductDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Alaeddine-OMRI
 *
 */
public class CustomDefaultProductDao implements CustomProductDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;




	@Override
	public double findByProduct(final ProductModel productModel)
	{
		final String queryString = "Select {oe:product} from {OrderEntry as oe} where {oe:product}='" + productModel.getPk()
				+ "'";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return flexibleSearchService.<ProductModel> search(query).getResult().size();
	}




	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}




	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}



}
