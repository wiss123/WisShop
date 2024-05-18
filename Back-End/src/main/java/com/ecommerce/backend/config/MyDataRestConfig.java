package com.ecommerce.backend.config;

import com.ecommerce.backend.entity.Country;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.ProductCategory;
import com.ecommerce.backend.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
 @RequiredArgsConstructor
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;


        @Override
        public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

            HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

            // disable HTTP methods for ProductCategory: PUT, POST, DELETE and PATCH
            disableHttpMethods(ProductCategory.class,config, theUnsupportedActions);
            disableHttpMethods(Product.class,config, theUnsupportedActions);
            disableHttpMethods(Country.class,config, theUnsupportedActions);
            disableHttpMethods(State.class,config, theUnsupportedActions);

            //call an internal helper method
            exposeIds(config);
        }

    private void disableHttpMethods(Class theClass,RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    //add id

        private void exposeIds(RepositoryRestConfiguration config) {
             //expose entity ids
            // get a list of all entity manager classes from the entity manager

           Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

            // create an array of the entity types
            List<Class> entityClasses = new ArrayList<>();

            //get the entity types for the entities
            for(EntityType tempEntityType : entities){
                entityClasses.add(tempEntityType.getJavaType());
            }
            // expose thw entity ids for the eìarray of entity/domain types
            Class[] domainTypes = entityClasses.toArray(new Class[0]);
            config.exposeIdsFor(domainTypes);
        }


}
