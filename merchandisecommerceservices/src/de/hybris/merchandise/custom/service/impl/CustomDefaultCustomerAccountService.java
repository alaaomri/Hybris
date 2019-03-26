/**
 *
 */
package de.hybris.merchandise.custom.service.impl;
import de.hybris.merchandise.customer.service.CustomCustomerAccountService;
import de.hybris.platform.commerceservices.customer.*;
import de.hybris.platform.commerceservices.customer.dao.CustomerAccountDao;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @author Alaeddine-OMRI
 *
 */
public class CustomDefaultCustomerAccountService implements CustomCustomerAccountService
{
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CustomDefaultCustomerAccountService.class);
	private static final String DEFAULT_PASSWORD_ENCODING = "md5";
	private UserService userService;
	private CustomerAccountService customerAccountService;
	private PaymentService paymentService;
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	private I18NService i18nService;
	private PasswordEncoderService passwordEncoderService;
	private SecureTokenService secureTokenService;
	private long tokenValiditySeconds;
	private CustomerAccountDao customerAccountDao;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private EventService eventService;
	private CommonI18NService commonI18NService;
	private TypeService typeService;
	private CustomerEmailResolutionService customerEmailResolutionService;
	private String passwordEncoding = DEFAULT_PASSWORD_ENCODING;
	private CustomerNameStrategy customerNameStrategy;
	private String monthsForOrderExpiry;
	private TimeService timeService;


	@Override
	public void updateProfile(final CustomerModel customerModel, final String titleCode, final String name, final String login,
			final String postalCode) throws DuplicateUidException
	{
		
		customerModel.setUid(login);
		customerModel.setName(name);
		customerModel.setPostalCode(postalCode);
		if (StringUtils.isNotBlank(titleCode))
		{
			customerModel.setTitle(getUserService().getTitleForCode(titleCode));
		}
		else
		{
			customerModel.setTitle(null);
		}
		internalSaveCustomer(customerModel);
	}
	protected void internalSaveCustomer(final CustomerModel customerModel) throws DuplicateUidException
	{
		try
		{
			getModelService().save(customerModel);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				throw new DuplicateUidException(customerModel.getUid(), e);
			}
			else
			{
				throw e;
			}
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new DuplicateUidException(customerModel.getUid(), e);
		}
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}


	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}


	/**
	 * @param customerAccountService the customerAccountService to set
	 */
	public void setCustomerAccountService(CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}


	/**
	 * @return the paymentService
	 */
	public PaymentService getPaymentService()
	{
		return paymentService;
	}


	/**
	 * @param paymentService the paymentService to set
	 */
	public void setPaymentService(PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService the modelService to set
	 */
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	/**
	 * @return the i18nService
	 */
	public I18NService getI18nService()
	{
		return i18nService;
	}


	/**
	 * @param i18nService the i18nService to set
	 */
	public void setI18nService(I18NService i18nService)
	{
		this.i18nService = i18nService;
	}


	/**
	 * @return the passwordEncoderService
	 */
	public PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}


	/**
	 * @param passwordEncoderService the passwordEncoderService to set
	 */
	public void setPasswordEncoderService(PasswordEncoderService passwordEncoderService)
	{
		this.passwordEncoderService = passwordEncoderService;
	}


	/**
	 * @return the secureTokenService
	 */
	public SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}


	/**
	 * @param secureTokenService the secureTokenService to set
	 */
	public void setSecureTokenService(SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}


	/**
	 * @return the tokenValiditySeconds
	 */
	public long getTokenValiditySeconds()
	{
		return tokenValiditySeconds;
	}


	/**
	 * @param tokenValiditySeconds the tokenValiditySeconds to set
	 */
	public void setTokenValiditySeconds(long tokenValiditySeconds)
	{
		this.tokenValiditySeconds = tokenValiditySeconds;
	}


	/**
	 * @return the customerAccountDao
	 */
	public CustomerAccountDao getCustomerAccountDao()
	{
		return customerAccountDao;
	}


	/**
	 * @param customerAccountDao the customerAccountDao to set
	 */
	public void setCustomerAccountDao(CustomerAccountDao customerAccountDao)
	{
		this.customerAccountDao = customerAccountDao;
	}


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService the baseStoreService to set
	 */
	public void setBaseStoreService(BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}


	/**
	 * @param baseSiteService the baseSiteService to set
	 */
	public void setBaseSiteService(BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}


	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}


	/**
	 * @param eventService the eventService to set
	 */
	public void setEventService(EventService eventService)
	{
		this.eventService = eventService;
	}


	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}


	/**
	 * @param commonI18NService the commonI18NService to set
	 */
	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}


	/**
	 * @return the typeService
	 */
	public TypeService getTypeService()
	{
		return typeService;
	}


	/**
	 * @param typeService the typeService to set
	 */
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}


	/**
	 * @return the customerEmailResolutionService
	 */
	public CustomerEmailResolutionService getCustomerEmailResolutionService()
	{
		return customerEmailResolutionService;
	}


	/**
	 * @param customerEmailResolutionService the customerEmailResolutionService to set
	 */
	public void setCustomerEmailResolutionService(CustomerEmailResolutionService customerEmailResolutionService)
	{
		this.customerEmailResolutionService = customerEmailResolutionService;
	}


	/**
	 * @return the passwordEncoding
	 */
	public String getPasswordEncoding()
	{
		return passwordEncoding;
	}


	/**
	 * @param passwordEncoding the passwordEncoding to set
	 */
	public void setPasswordEncoding(String passwordEncoding)
	{
		this.passwordEncoding = passwordEncoding;
	}


	/**
	 * @return the customerNameStrategy
	 */
	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}


	/**
	 * @param customerNameStrategy the customerNameStrategy to set
	 */
	public void setCustomerNameStrategy(CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}


	/**
	 * @return the monthsForOrderExpiry
	 */
	public String getMonthsForOrderExpiry()
	{
		return monthsForOrderExpiry;
	}


	/**
	 * @param monthsForOrderExpiry the monthsForOrderExpiry to set
	 */
	public void setMonthsForOrderExpiry(String monthsForOrderExpiry)
	{
		this.monthsForOrderExpiry = monthsForOrderExpiry;
	}


	/**
	 * @return the timeService
	 */
	public TimeService getTimeService()
	{
		return timeService;
	}


	/**
	 * @param timeService the timeService to set
	 */
	public void setTimeService(TimeService timeService)
	{
		this.timeService = timeService;
	}


	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}


	/**
	 * @return the defaultPasswordEncoding
	 */
	public static String getDefaultPasswordEncoding()
	{
		return DEFAULT_PASSWORD_ENCODING;
	}




}
