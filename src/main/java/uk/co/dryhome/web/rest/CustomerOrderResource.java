package uk.co.dryhome.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.search.CustomerOrderSearchRepository;
import uk.co.dryhome.web.rest.util.HeaderUtil;
import uk.co.dryhome.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CustomerOrder.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrderResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);
        
    @Inject
    private CustomerOrderRepository customerOrderRepository;
    
    @Inject
    private CustomerOrderSearchRepository customerOrderSearchRepository;
    
    /**
     * POST  /customerOrders -> Create a new customerOrder.
     */
    @RequestMapping(value = "/customerOrders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerOrder> createCustomerOrder(@Valid @RequestBody CustomerOrder customerOrder) throws URISyntaxException {
        log.debug("REST request to save CustomerOrder : {}", customerOrder);
        if (customerOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerOrder", "idexists", "A new customerOrder cannot already have an ID")).body(null);
        }
        CustomerOrder result = customerOrderRepository.save(customerOrder);
        customerOrderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/customerOrders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customerOrders -> Updates an existing customerOrder.
     */
    @RequestMapping(value = "/customerOrders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerOrder> updateCustomerOrder(@Valid @RequestBody CustomerOrder customerOrder) throws URISyntaxException {
        log.debug("REST request to update CustomerOrder : {}", customerOrder);
        if (customerOrder.getId() == null) {
            return createCustomerOrder(customerOrder);
        }
        CustomerOrder result = customerOrderRepository.save(customerOrder);
        customerOrderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerOrder", customerOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customerOrders -> get all the customerOrders.
     */
    @RequestMapping(value = "/customerOrders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CustomerOrder>> getAllCustomerOrders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CustomerOrders");
        Page<CustomerOrder> page = customerOrderRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customerOrders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customerOrders/:id -> get the "id" customerOrder.
     */
    @RequestMapping(value = "/customerOrders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerOrder> getCustomerOrder(@PathVariable Long id) {
        log.debug("REST request to get CustomerOrder : {}", id);
        CustomerOrder customerOrder = customerOrderRepository.findOne(id);
        return Optional.ofNullable(customerOrder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customerOrders/:id -> delete the "id" customerOrder.
     */
    @RequestMapping(value = "/customerOrders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerOrder(@PathVariable Long id) {
        log.debug("REST request to delete CustomerOrder : {}", id);
        customerOrderRepository.delete(id);
        customerOrderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerOrder", id.toString())).build();
    }

    /**
     * SEARCH  /_search/customerOrders/:query -> search for the customerOrder corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/customerOrders/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CustomerOrder> searchCustomerOrders(@PathVariable String query) {
        log.debug("REST request to search CustomerOrders for query {}", query);
        return StreamSupport
            .stream(customerOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
