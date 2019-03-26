/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.merchandisefacades.customer.custom.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.merchandise.customer.service.CustomCustomerAccountService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.strategies.CartCleanStrategy;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * Default implementation for the {@link CustomerFacade}.
 */
public class CustomDefaultCustomerFacade extends DefaultCustomerFacade
{


	private static final Logger LOG = Logger.getLogger(DefaultCustomerFacade.class);

	private UserService userService;
	private CustomCustomerAccountService customCustomerAccountService;
	private CustomerAccountService customerAccountService;


	private CommonI18NService commonI18NService;
	private ModelService modelService;
	private StoreSessionFacade storeSessionFacade;
	private CommerceCartService commerceCartService;
	private CartService cartService;
	private SessionService sessionService;
	private UserFacade userFacade;
	private OrderFacade orderFacade;

	private Populator<AddressData, AddressModel> addressReversePopulator;
	private Populator<CustomerData, UserModel> customerReversePopulator;
	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	private Converter<UserModel, CustomerData> customerConverter;
	private Converter<TitleModel, TitleData> titleConverter;

	private CustomerNameStrategy customerNameStrategy;
	private PasswordEncoderService passwordEncoderService;
	private CartCleanStrategy cartCleanStrategy;

	@Override
	public void register(final RegisterData registerData) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("registerData", registerData);
		Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");
		Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");

		final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
		newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));

		if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
		{
			newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
		}
		final TitleModel title = getUserService().getTitleForCode(registerData.getTitleCode());
		newCustomer.setTitle(title);
		newCustomer.setPostalCode(registerData.getPostalCode());
		setUidForRegister(registerData, newCustomer);
		newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		getCustomerAccountService().register(newCustomer, registerData.getPassword());
	}



	@Override
	public void updateProfile(final CustomerData customerData) throws DuplicateUidException
	{
		validateDataBeforeUpdate(customerData);

		final String name = getCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
		final CustomerModel customer = getCurrentSessionCustomer();
		customer.setOriginalUid(customerData.getDisplayUid());
		getCustomCustomerAccountService().updateProfile(customer, customerData.getTitleCode(), name, customerData.getUid(),
				customerData.getPostalCode());
	}



	/**
	 * @return the userService
	 */
	@Override
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Override
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */


	/**
	 * @return the commonI18NService
	 */
	@Override
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @return the customCustomerAccountService
	 */
	public CustomCustomerAccountService getCustomCustomerAccountService()
	{
		return customCustomerAccountService;
	}



	/**
	 * @param customCustomerAccountService
	 *           the customCustomerAccountService to set
	 */
	public void setCustomCustomerAccountService(final CustomCustomerAccountService customCustomerAccountService)
	{
		this.customCustomerAccountService = customCustomerAccountService;
	}



	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the storeSessionFacade
	 */
	@Override
	public StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	/**
	 * @param storeSessionFacade
	 *           the storeSessionFacade to set
	 */
	@Override
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	/**
	 * @return the commerceCartService
	 */
	@Override
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	@Override
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	/**
	 * @return the cartService
	 */
	@Override
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Override
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the sessionService
	 */
	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	@Override
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the userFacade
	 */
	@Override
	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	@Override
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	/**
	 * @return the orderFacade
	 */
	@Override
	public OrderFacade getOrderFacade()
	{
		return orderFacade;
	}

	/**
	 * @param orderFacade
	 *           the orderFacade to set
	 */
	@Override
	public void setOrderFacade(final OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}

	/**
	 * @return the addressReversePopulator
	 */
	@Override
	public Populator<AddressData, AddressModel> getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/**
	 * @param addressReversePopulator
	 *           the addressReversePopulator to set
	 */
	@Override
	public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
	}

	/**
	 * @return the customerReversePopulator
	 */
	@Override
	public Populator<CustomerData, UserModel> getCustomerReversePopulator()
	{
		return customerReversePopulator;
	}

	/**
	 * @param customerReversePopulator
	 *           the customerReversePopulator to set
	 */
	@Override
	public void setCustomerReversePopulator(final Populator<CustomerData, UserModel> customerReversePopulator)
	{
		this.customerReversePopulator = customerReversePopulator;
	}

	/**
	 * @return the addressConverter
	 */
	@Override
	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	@Override
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	/**
	 * @return the creditCardPaymentInfoConverter
	 */
	@Override
	public Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter()
	{
		return creditCardPaymentInfoConverter;
	}

	/**
	 * @param creditCardPaymentInfoConverter
	 *           the creditCardPaymentInfoConverter to set
	 */
	@Override
	public void setCreditCardPaymentInfoConverter(
			final Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter)
	{
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	/**
	 * @return the customerConverter
	 */
	@Override
	public Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	/**
	 * @param customerConverter
	 *           the customerConverter to set
	 */
	@Override
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	/**
	 * @return the titleConverter
	 */
	@Override
	public Converter<TitleModel, TitleData> getTitleConverter()
	{
		return titleConverter;
	}

	/**
	 * @param titleConverter
	 *           the titleConverter to set
	 */
	@Override
	public void setTitleConverter(final Converter<TitleModel, TitleData> titleConverter)
	{
		this.titleConverter = titleConverter;
	}

	/**
	 * @return the customerNameStrategy
	 */
	@Override
	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	/**
	 * @param customerNameStrategy
	 *           the customerNameStrategy to set
	 */
	@Override
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	/**
	 * @return the passwordEncoderService
	 */
	@Override
	public PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}

	/**
	 * @param passwordEncoderService
	 *           the passwordEncoderService to set
	 */
	@Override
	public void setPasswordEncoderService(final PasswordEncoderService passwordEncoderService)
	{
		this.passwordEncoderService = passwordEncoderService;
	}

	/**
	 * @return the cartCleanStrategy
	 */
	@Override
	public CartCleanStrategy getCartCleanStrategy()
	{
		return cartCleanStrategy;
	}

	/**
	 * @param cartCleanStrategy
	 *           the cartCleanStrategy to set
	 */
	@Override
	public void setCartCleanStrategy(final CartCleanStrategy cartCleanStrategy)
	{
		this.cartCleanStrategy = cartCleanStrategy;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}

	/**
	 * @return the customerAccountService
	 */
	@Override
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}



	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	@Override
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}
}
