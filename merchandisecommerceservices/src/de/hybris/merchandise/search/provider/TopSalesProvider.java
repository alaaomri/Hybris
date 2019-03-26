/**
 *
 */
package de.hybris.merchandise.search.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.util.List;


/**
 * @author Alaeddine-OMRI
 *
 */
public interface TopSalesProvider
{
	List<FacetValue> getTopValues(IndexedProperty indexedProperty, List<FacetValue> facets);
}
