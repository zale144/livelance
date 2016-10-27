package livelance.app.service;

import livelance.app.model.Category;
import livelance.app.model.list.CategoryList;

public interface CategoryService {

	/**
	 * 
	 * @return a list of all existing Categories
	 */
	CategoryList findAll();

	/**
	 * Returns a Category with the specified ID.
	 * 
	 * @param id
	 *            ID of the Category
	 * @return a Category if Category with such ID exists {@code null} if
	 *         Category is not found.
	 */
	Category findOne(Long id);

	/**
	 * Persists the Category. If ID is null, a new ID will be assigned
	 * automatically.
	 * 
	 * @param ID
	 * @return Category state after persisting.
	 */
	Category save(Category category);

	/**
	 * Deletes a Category having specified ID.
	 * 
	 * @param id
	 *            ID of the Category to be deleted
	 * @return deleted Category if it was deleted successfully. If deleting was
	 *         unsuccessful, an {@link IllegalArgumentException} is thrown.
	 */
	Category delete(Long id);

	/**
	 * 
	 * @param name
	 *            the name of the Category
	 * @return a list of Categories who's name matches the given {@code name}
	 *         parameter.
	 */
	CategoryList findByName(String name);

	/**
	 * 
	 * @param name
	 *            the name of the Category
	 * @return a list of Categories who's name is like the given {@code name}
	 *         parameter.
	 */
	CategoryList findByNameLike(String name);

	/**
	 * Evicts all members of the "categories" cache.
	 */
	void evictCache();

}
