package livelance.app.service;

import livelance.app.model.Profile;
import livelance.app.model.list.ProfileList;

public interface ProfileService {
	/**
	 * 
	 * @return a list of all existing profiles
	 */
	ProfileList findAll();

	/**
	 * Returns a Profile with the specified ID.
	 * 
	 * @param id
	 *            ID of the Profile
	 * @return a Profile if Account with such ID exists {@code null} if Profile is not
	 *         found.
	 */
	Profile findOne(Long id);

	/**
	 * Persists the Profile. If ID is null, a new ID will be assigned
	 * automatically.
	 * 
	 * @param profile
	 * @return Profile state after persisting.
	 */
	Profile save(Profile profile);
	
	/**
	 * Deletes a Profile having specified ID.
	 * 
	 * @param id
	 *            ID of the Profile to be deleted
	 * @return deleted Account if it was deleted successfully. If deleting was
	 *         unsuccessful, an {@link IllegalArgumentException} is thrown.
	 */
	Profile delete(Long id);

	/**
	 * @param namePart1
	 *            firstname or lastname of the Profile
	 * @param namePart2
	 *            firstname or lastname of the Profile
	 * @return a list of Accounts who's name and lastname matches the given
	 *         {@code namePart1} and {@code namePart2} parameters, respectively.
	 */
     ProfileList findByFirstNameAndLastName(String namePart1, String namePart2);

	/**
	 * @param name
	 *            name of the Profile
	 * @return a list of Profile who's firstname or lastname
	 *  is like the given name
	 *         {@code name} parameter
	 */
     ProfileList findByNameLike(String name);
	
	/**
	 * Evicts all members of the "profiles" cache.
	 */
	void evictCache();
}
