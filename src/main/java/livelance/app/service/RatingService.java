package livelance.app.service;

import livelance.app.model.Rating;
import livelance.app.model.list.RatingList;

public interface RatingService {

	/**
	 * 
	 * @param dealId ID of the Deal it belongs to
	 * @return a list of all existing Ratings
	 */
	RatingList findAll(Long dealId);

	/**
	 * Returns a Rating with the specified ID.
	 * 
	 * @param id
	 *            ID of the Rating
	 * @param dealId  ID of the Deal it belongs to
	 * @return a Rating if Rating with such ID exists {@code null} if Rating is
	 *         not found.
	 */
	Rating findOne(Long id, Long dealId);

	/**
	 * Persists the Rating. If ID is null, a new ID will be assigned
	 * automatically.
	 * 
	 * @param ID
	 * @return Rating state after persisting.
	 */
	Rating save(Rating rating);

	/**
	 * Deletes a Rating having specified ID.
	 * 
	 * @param id
	 *            ID of the Rating to be deleted
	 * @param dealId ID of the Deal it belongs to
	 * @return deleted Rating if it was deleted successfully. If deleting was
	 *         unsuccessful, an {@link IllegalArgumentException} is thrown.
	 */
	Rating delete(Long id, Long dealId);

	/**
	 * Evicts all members of the "ratings" cache.
	 */
	void evictCache();
}
