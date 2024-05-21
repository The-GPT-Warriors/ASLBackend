package com.nighthawk.spring_portfolio.mvc.generalization;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneralizationRepository extends MongoRepository<Generalization, String> {

}
