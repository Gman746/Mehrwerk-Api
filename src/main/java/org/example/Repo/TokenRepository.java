package org.example.Repo;

import org.example.Model.ApiToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<ApiToken, Long> {

}
