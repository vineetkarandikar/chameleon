package com.digital.chameleon.security.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.digital.chameleon.security.entity.OtpRequest;

@Repository
public interface OtpRepository extends CrudRepository<OtpRequest, String> {

	public List<OtpRequest> findByEmailId(String emailId);

	public List<OtpRequest> findByMobile(String mobile);

}
