package com.digital.chameleon.security.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.digital.chameleon.security.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	public User findByMobile(String mobile);

	public User findByEmailId(String emailId);
	
	public User findByIdAndType(String id,String type);

}
