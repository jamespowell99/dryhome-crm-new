package uk.co.dryhome.repository;

import uk.co.dryhome.domain.CustomerOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerOrder entity.
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {

}
