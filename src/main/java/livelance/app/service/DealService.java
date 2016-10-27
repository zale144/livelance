package livelance.app.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import livelance.app.model.Circle;
import livelance.app.model.Deal;
import livelance.app.model.list.DealList;

public interface DealService {

	/**
	 * 
	 * @return a list of all existing Deals
	 */
//	@PreAuthorize("permitAll")
	DealList findAll(Circle circle);

	/**
	 * Returns a Deal with the specified ID.
	 * 
	 * @param id
	 *            ID of the Deal
	 * @return a Deal if Deal with such ID exists {@code null} if Deal is not
	 *         found.
	 */
	Deal findOne(Long id);

	/**
	 * Returns a list of Deals with the specified Account.
	 * 
	 * @param AccountResource
	 *            of the Deal
	 * @return a list of Deals if Deals with such Account exists {@code null} if
	 *         Deal is not found.
	 */
	DealList findByProfileId(Long id);

	/**
	 * Returns a list of Deals with the specified Service.
	 * 
	 * @param serviceId
	 *            of the Deal
	 * @return a list of Deals if Deals with such Service exist {@code null} if
	 *         Deal is not found.
	 */
	DealList findByServiceId(Long serviceId);
	
	/**
	 * Returns a list of Deals with the specified Service.
	 * 
	 * @param serviceName
	 *            of the Deal
	 * @return a list of Deals if Deals with such Service exist {@code null} if
	 *         Deal is not found.
	 */
	DealList findByServiceName(String serviceName, Circle circle);
	
	/**
	 * Returns a list of Deals with the specified Category.
	 * 
	 * @param categoryName
	 *            of the Deal
	 * @return a list of Deals if Deals with such Category exist {@code null} if
	 *         Deal is not found.
	 */
	DealList findbyCategoryName(String categoryName, Circle circle);

	/**
	 * Persists the Deal. If ID is null, a new ID will be assigned
	 * automatically.
	 * 
	 * @param ID
	 * @return Deal state after persisting.
	 */
	Deal save(Deal deal);

	/**
	 * Deletes a Deal having specified ID.
	 * 
	 * @param id
	 *            ID of the Deal to be deleted
	 * @return deleted Deal if it was deleted successfully. If deleting was
	 *         unsuccessful, an {@link IllegalArgumentException} is thrown.
	 */
	Deal delete(Long id);
	
	/**
	 * Deletes a list of Deals 
	 * 
	 * @param ids A {@link List} of activity IDs (each ID is of type {@link Long})
	 */
	void delete(List<Deal> deals);

	/**
	 * Returns a list of Deals with the specified description.
	 * 
	 * @param description
	 *            of the Deal
	 * @return a list of Deals if Deals with such description exist {@code null}
	 *         if Deal is not found.
	 */
	DealList findByDescription(String description);

	/**
	 * Returns a list of Deals with similar description.
	 * 
	 * @param description
	 *            of the Deal
	 * @return a list of Deals if Deals with similar description exist
	 *         {@code null} if Deal is not found.
	 */
	DealList findBySearchTerm(String description, Circle circle);

	/**
	 * Evicts all members of the "deals" cache.
	 */
	void evictCache();

}
