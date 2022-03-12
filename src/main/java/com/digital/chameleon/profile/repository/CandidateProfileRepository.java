package com.digital.chameleon.profile.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.digital.chameleon.profile.model.CandidateProfile;

public interface CandidateProfileRepository extends CrudRepository<CandidateProfile, String> {
  public List<CandidateProfile> findByResumeTextLikeOrderByLastUpdatedDesc(String resumeText);

}
