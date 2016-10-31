	package livelance.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import livelance.app.model.Account;
import livelance.app.model.list.AccountList;
import livelance.app.repository.AccountRepository;
import livelance.app.service.AccountService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JpaAccountService implements AccountService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AccountRepository accountRepository;

	@Override
	public AccountList findAll() {
		logger.info("> findAll");
		AccountList accounts = new AccountList(accountRepository.findAll());
		logger.info("< findAll");
		return accounts;
	}

	@Override
	@Cacheable(value = "accounts", key = "#id")
	public Account findOne(Long id) {
		logger.info("> findOne id:{}", id);
		Account account = accountRepository.findOne(id);
		if (account == null) {
			logger.error("Account not found.");
		}
		logger.info("< findOne id:{}", id);
		return account;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "accounts", key = "#id")
	public Account delete(Long id) {
		logger.info("> delete id:{}", id);

		Account found = findOne(id);
		if (found == null) {
			logger.error("Attempted to delete a Account, but the entity does not exist.");
		} else {
			accountRepository.delete(found);
		}
		logger.info("< delete id:{}", id);
		return found;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "accounts", key = "#result.id")
	public Account save(Account account) {
		logger.info("> save");
		if(account == null) {
			return null;
		}
		Account savedAccount = accountRepository.save(account);
		if (savedAccount == null) {
			logger.error("Account not saved.");
		}
		logger.info("< save");
		return savedAccount;
	}

	@Override // RETURNS Account - for Security
	@Cacheable(value = "accounts", key = "#username")
	public Account findByUsername(String username) {
		logger.info("> findByUsername username:{}", username);
		
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			logger.error("Account not found.");
			return null;
		} 
		logger.info("< findByUsername username:{}", account.getUsername());
		return account;
	}
	
	@Override
    @CacheEvict(
            value = "accounts",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }

}
