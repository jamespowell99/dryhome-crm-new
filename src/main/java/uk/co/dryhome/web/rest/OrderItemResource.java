package uk.co.dryhome.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.co.dryhome.domain.OrderItem;
import uk.co.dryhome.repository.OrderItemRepository;
import uk.co.dryhome.repository.search.OrderItemSearchRepository;
import uk.co.dryhome.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing OrderItem.
 */
@RestController
@RequestMapping("/api")
public class OrderItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemResource.class);
        
    @Inject
    private OrderItemRepository orderItemRepository;
    
    @Inject
    private OrderItemSearchRepository orderItemSearchRepository;
    
    /**
     * POST  /orderItems -> Create a new orderItem.
     */
    @RequestMapping(value = "/orderItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to save OrderItem : {}", orderItem);
        if (orderItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderItem", "idexists", "A new orderItem cannot already have an ID")).body(null);
        }
        OrderItem result = orderItemRepository.save(orderItem);
        orderItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/orderItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orderItems -> Updates an existing orderItem.
     */
    @RequestMapping(value = "/orderItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderItem> updateOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to update OrderItem : {}", orderItem);
        if (orderItem.getId() == null) {
            return createOrderItem(orderItem);
        }
        OrderItem result = orderItemRepository.save(orderItem);
        orderItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderItem", orderItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orderItems -> get all the orderItems.
     */
    @RequestMapping(value = "/orderItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderItem> getAllOrderItems() {
        log.debug("REST request to get all OrderItems");
        return orderItemRepository.findAll();
            }

    /**
     * GET  /orderItems/:id -> get the "id" orderItem.
     */
    @RequestMapping(value = "/orderItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable Long id) {
        log.debug("REST request to get OrderItem : {}", id);
        OrderItem orderItem = orderItemRepository.findOne(id);
        return Optional.ofNullable(orderItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /orderItems/:id -> delete the "id" orderItem.
     */
    @RequestMapping(value = "/orderItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderItem : {}", id);
        orderItemRepository.delete(id);
        orderItemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/orderItems/:query -> search for the orderItem corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/orderItems/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderItem> searchOrderItems(@PathVariable String query) {
        log.debug("REST request to search OrderItems for query {}", query);
        return StreamSupport
            .stream(orderItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
