package livelance.app.service;

import livelance.app.model.Circle;
import livelance.app.model.Service;
import livelance.app.model.list.ServiceList;

public interface ServiceService {

	/**
	 * 
	 * @param catId ID of the Category
	 * @return a list of all existing Service
	 */
	ServiceList findAll(Long catId);
	
	/**
	 * Returns a Service with the specified ID.
	 * @param id ID of the Service
	 * @param catId ID of the Category where Service 
	 * belongs
	 * @return a Service if Service with such ID exists
	 * {@code null} if Service is not found.
	 */
	Service findOne(Long id, Long catId);
	/**
	 * Returns a Service with the specified name.
	 * @param name name of the Service
	 * @return a Service if Service with such name exists
	 * {@code null} if Service is not found.
	 */
	Service findByName(String name);
	
	/**
	 * Returns a ServiceList with the specified 
	 * Category name.
	 * @param categoryId name of the Category
	 * @return a ServiceList if Services with such Category
	 * name exist
	 * {@code null} if no Services are found.
	 */
	ServiceList findbyCategoryName(String categoryName, Circle circle);
	
	/**
	 * Persists the Service. If ID is null, a new ID 
	 * will be assigned automatically.
	 * @param ID
	 * @return Service state after persisting.
	 */
	Service save(Service service);
	
	/**
	 * Deletes a Service having specified ID.
	 * @param id ID of the Service to be deleted
	 * @param catId ID of the Category where Service 
	 * belongs
	 * @return deleted Service if it was deleted 
	 * successfully. If deleting was unsuccessful,
	 * an {@link IllegalArgumentException} is thrown.
	 */
	Service delete(Long id, Long catID);

		 /**
     * Evicts all members of the "services" cache.
     */
    void evictCache();
}
