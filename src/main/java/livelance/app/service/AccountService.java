package livelance.app.service;

import org.springframework.security.access.prepost.PreAuthorize;

import livelance.app.model.Account;
import livelance.app.model.list.AccountList;

public interface AccountService {

	/**
	 * 
	 * @return a list of all existing users
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	AccountList findAll();

	/**
	 * Returns a Account with the specified ID.
	 * 
	 * @param id
	 *            ID of the Account
	 * @return a Account if Account with such ID exists {@code null} if Account is not
	 *         found.
	 */
	Account findOne(Long id);

	/**
	 * Persists the Account. If ID is null, a new ID will be assigned
	 * automatically.
	 * 
	 * @param account
	 * @return Account state after persisting.
	 */
	Account save(Account account);
	

	/**
	 * Deletes a Account having specified ID.
	 * 
	 * @param id
	 *            ID of the Account to be deleted
	 * @return deleted Account if it was deleted successfully. If deleting was
	 *         unsuccessful, an {@link IllegalArgumentException} is thrown.
	 */
	Account delete(Long id);

	/**
	 * @param username
	 *            username of the Account
	 * @return an Account who's is username matches the given name
	 *         {@code username} parameter
	 */
	Account findByUsername(String username);

	/**
	 * Evicts all members of the "accounts" cache.
	 */
	void evictCache();

}
