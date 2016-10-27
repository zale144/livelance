package livelance.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import livelance.app.model.Role;
import livelance.app.repository.RoleRepository;
import livelance.app.service.RoleService;

@Service
public class JpaRoleService implements RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Role getRole(Long id) {
		return roleRepository.findOne(id);
	}

}
