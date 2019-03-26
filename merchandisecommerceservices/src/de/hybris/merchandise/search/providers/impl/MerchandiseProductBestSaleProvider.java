/**
 *
 */
package de.hybris.merchandise.search.providers.impl;

import de.hybris.merchandise.customer.service.CustomProductService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author Alaeddine-OMRI
 *
 */
public class MerchandiseProductBestSaleProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable

{
	private static final Logger LOG = Logger.getLogger(MerchandiseProductBestSaleProvider.class);

	private FieldNameProvider fieldNameProvider;
	private CustomProductService customProductService;


	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			if (indexedProperty.isLocalized())
			{
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages)
				{
					fieldValues.addAll(createFieldValue(product, language, indexedProperty));
				}
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, null, indexedProperty));
			}
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate rating of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final LanguageModel language,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Double numberOfTimesSaled = getCustomProductService().getNumberOfProductSaled(product, language);
		if (numberOfTimesSaled != null)
		{
			addFieldValues(fieldValues, indexedProperty, language, numberOfTimesSaled);
		}

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}



	/**
	 * @return the customProductService
	 */
	public CustomProductService getCustomProductService()
	{
		return customProductService;
	}

	/**
	 * @param customProductService
	 *           the customProductService to set
	 */
	public void setCustomProductService(final CustomProductService customProductService)
	{
		this.customProductService = customProductService;
	}

	/**
	 * @return the fieldNameProvider
	 */
	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	/**
	 * @param fieldNameProvider
	 *           the fieldNameProvider to set
	 */
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}
