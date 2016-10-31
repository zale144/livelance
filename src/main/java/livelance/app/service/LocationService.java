package livelance.app.service;

import livelance.app.model.Location;
import livelance.app.model.list.LocationList;

public interface LocationService {

	/**
	 * 
	 * @param dealId Id of the deal it belongs to
	 * @return a list of all existing Locations
	 */
	LocationList findAll(Long dealId);

	/**
	 * Returns a Location with the specified ID.
	 * 
	 * @param id
	 *            ID of the Location
	 * @param dealId Id of the Deal it belongs to
	 * @return a Location if Location with such ID exists {@code null} if
	 *         Location is not found.
	 */
	Location findOne(Long id, Long dealId);

	/**
	 * Persists the Location. If ID is null, a new ID will be assigned
	 * automatically.
	 * 
	 * @param ID
	 * @return Location state after persisting.
	 */
	Location save(Location location);

	/**
	 * Deletes a Location having specified ID.
	 * 
	 * @param id
	 *            ID of the Location to be deleted
	 * @param dealId ID of the Deal it belongs to
	 * @return deleted Location if it was deleted successfully. If deleting was
	 *         unsuccessful, an {@link IllegalArgumentException} is thrown.
	 */
	Location delete(Long id, Long dealId);

	/**
	 * Evicts all members of the "location" cache.
	 */
	void evictCache();

}
