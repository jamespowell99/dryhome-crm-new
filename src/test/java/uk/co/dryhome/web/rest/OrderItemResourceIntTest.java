package uk.co.dryhome.web.rest;

import uk.co.dryhome.Application;
import uk.co.dryhome.domain.OrderItem;
import uk.co.dryhome.repository.OrderItemRepository;
import uk.co.dryhome.repository.search.OrderItemSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OrderItemResource REST controller.
 *
 * @see OrderItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrderItemResourceIntTest {


    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final Integer DEFAULT_ORDER_INDEX = 1;
    private static final Integer UPDATED_ORDER_INDEX = 2;
    private static final String DEFAULT_SERIAL_NUMBER = "AAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBB";

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderItemSearchRepository orderItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderItemResource orderItemResource = new OrderItemResource();
        ReflectionTestUtils.setField(orderItemResource, "orderItemSearchRepository", orderItemSearchRepository);
        ReflectionTestUtils.setField(orderItemResource, "orderItemRepository", orderItemRepository);
        this.restOrderItemMockMvc = MockMvcBuilders.standaloneSetup(orderItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderItem = new OrderItem();
        orderItem.setPrice(DEFAULT_PRICE);
        orderItem.setQty(DEFAULT_QTY);
        orderItem.setNotes(DEFAULT_NOTES);
        orderItem.setOrderIndex(DEFAULT_ORDER_INDEX);
        orderItem.setSerialNumber(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    public void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // Create the OrderItem

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderItem.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testOrderItem.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testOrderItem.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testOrderItem.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setPrice(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isBadRequest());

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setQty(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isBadRequest());

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItems
        restOrderItemMockMvc.perform(get("/api/orderItems?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX)))
                .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/orderItems/{id}", orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/orderItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

		int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        orderItem.setPrice(UPDATED_PRICE);
        orderItem.setQty(UPDATED_QTY);
        orderItem.setNotes(UPDATED_NOTES);
        orderItem.setOrderIndex(UPDATED_ORDER_INDEX);
        orderItem.setSerialNumber(UPDATED_SERIAL_NUMBER);

        restOrderItemMockMvc.perform(put("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrderItem.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testOrderItem.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testOrderItem.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    public void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

		int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Get the orderItem
        restOrderItemMockMvc.perform(delete("/api/orderItems/{id}", orderItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
