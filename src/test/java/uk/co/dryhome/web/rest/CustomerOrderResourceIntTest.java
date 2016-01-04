package uk.co.dryhome.web.rest;

import uk.co.dryhome.Application;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.search.CustomerOrderSearchRepository;

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
 * Test class for the CustomerOrderResource REST controller.
 *
 * @see CustomerOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerOrderResourceIntTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";
    private static final String DEFAULT_CONTACT_TITLE = "AAAAA";
    private static final String UPDATED_CONTACT_TITLE = "BBBBB";
    private static final String DEFAULT_CONTACT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_CONTACT_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_CONTACT_SURNAME = "AAAAA";
    private static final String UPDATED_CONTACT_SURNAME = "BBBBB";
    private static final String DEFAULT_TEL = "AAAAA";
    private static final String UPDATED_TEL = "BBBBB";
    private static final String DEFAULT_MOB = "AAAAA";
    private static final String UPDATED_MOB = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_ADDRESS1 = "AAAAA";
    private static final String UPDATED_ADDRESS1 = "BBBBB";
    private static final String DEFAULT_ADDRESS2 = "AAAAA";
    private static final String UPDATED_ADDRESS2 = "BBBBB";
    private static final String DEFAULT_ADDRESS3 = "AAAAA";
    private static final String UPDATED_ADDRESS3 = "BBBBB";
    private static final String DEFAULT_TOWN = "AAAAA";
    private static final String UPDATED_TOWN = "BBBBB";
    private static final String DEFAULT_POST_CODE = "AAAAA";
    private static final String UPDATED_POST_CODE = "BBBBB";
    private static final String DEFAULT_PRODUCTS = "AAAAA";
    private static final String UPDATED_PRODUCTS = "BBBBB";
    private static final String DEFAULT_INTERESTED = "AAAAA";
    private static final String UPDATED_INTERESTED = "BBBBB";

    private static final BigDecimal DEFAULT_PAID = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAID = new BigDecimal(2);
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private CustomerOrderRepository customerOrderRepository;

    @Inject
    private CustomerOrderSearchRepository customerOrderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerOrderMockMvc;

    private CustomerOrder customerOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerOrderResource customerOrderResource = new CustomerOrderResource();
        ReflectionTestUtils.setField(customerOrderResource, "customerOrderSearchRepository", customerOrderSearchRepository);
        ReflectionTestUtils.setField(customerOrderResource, "customerOrderRepository", customerOrderRepository);
        this.restCustomerOrderMockMvc = MockMvcBuilders.standaloneSetup(customerOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerOrder = new CustomerOrder();
        customerOrder.setName(DEFAULT_NAME);
        customerOrder.setContactTitle(DEFAULT_CONTACT_TITLE);
        customerOrder.setContactFirstName(DEFAULT_CONTACT_FIRST_NAME);
        customerOrder.setContactSurname(DEFAULT_CONTACT_SURNAME);
        customerOrder.setTel(DEFAULT_TEL);
        customerOrder.setMob(DEFAULT_MOB);
        customerOrder.setEmail(DEFAULT_EMAIL);
        customerOrder.setAddress1(DEFAULT_ADDRESS1);
        customerOrder.setAddress2(DEFAULT_ADDRESS2);
        customerOrder.setAddress3(DEFAULT_ADDRESS3);
        customerOrder.setTown(DEFAULT_TOWN);
        customerOrder.setPostCode(DEFAULT_POST_CODE);
        customerOrder.setProducts(DEFAULT_PRODUCTS);
        customerOrder.setInterested(DEFAULT_INTERESTED);
        customerOrder.setPaid(DEFAULT_PAID);
        customerOrder.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createCustomerOrder() throws Exception {
        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();

        // Create the CustomerOrder

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrder)))
                .andExpect(status().isCreated());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeCreate + 1);
        CustomerOrder testCustomerOrder = customerOrders.get(customerOrders.size() - 1);
        assertThat(testCustomerOrder.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerOrder.getContactTitle()).isEqualTo(DEFAULT_CONTACT_TITLE);
        assertThat(testCustomerOrder.getContactFirstName()).isEqualTo(DEFAULT_CONTACT_FIRST_NAME);
        assertThat(testCustomerOrder.getContactSurname()).isEqualTo(DEFAULT_CONTACT_SURNAME);
        assertThat(testCustomerOrder.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCustomerOrder.getMob()).isEqualTo(DEFAULT_MOB);
        assertThat(testCustomerOrder.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerOrder.getAddress1()).isEqualTo(DEFAULT_ADDRESS1);
        assertThat(testCustomerOrder.getAddress2()).isEqualTo(DEFAULT_ADDRESS2);
        assertThat(testCustomerOrder.getAddress3()).isEqualTo(DEFAULT_ADDRESS3);
        assertThat(testCustomerOrder.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testCustomerOrder.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testCustomerOrder.getProducts()).isEqualTo(DEFAULT_PRODUCTS);
        assertThat(testCustomerOrder.getInterested()).isEqualTo(DEFAULT_INTERESTED);
        assertThat(testCustomerOrder.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setName(null);

        // Create the CustomerOrder, which fails.

        restCustomerOrderMockMvc.perform(post("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrder)))
                .andExpect(status().isBadRequest());

        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerOrders() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrders
        restCustomerOrderMockMvc.perform(get("/api/customerOrders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].contactTitle").value(hasItem(DEFAULT_CONTACT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].contactFirstName").value(hasItem(DEFAULT_CONTACT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].contactSurname").value(hasItem(DEFAULT_CONTACT_SURNAME.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].mob").value(hasItem(DEFAULT_MOB.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS1.toString())))
                .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS2.toString())))
                .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS3.toString())))
                .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
                .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
                .andExpect(jsonPath("$.[*].products").value(hasItem(DEFAULT_PRODUCTS.toString())))
                .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED.toString())))
                .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get("/api/customerOrders/{id}", customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerOrder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contactTitle").value(DEFAULT_CONTACT_TITLE.toString()))
            .andExpect(jsonPath("$.contactFirstName").value(DEFAULT_CONTACT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.contactSurname").value(DEFAULT_CONTACT_SURNAME.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.mob").value(DEFAULT_MOB.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS2.toString()))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS3.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.products").value(DEFAULT_PRODUCTS.toString()))
            .andExpect(jsonPath("$.interested").value(DEFAULT_INTERESTED.toString()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerOrder() throws Exception {
        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get("/api/customerOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

		int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();

        // Update the customerOrder
        customerOrder.setName(UPDATED_NAME);
        customerOrder.setContactTitle(UPDATED_CONTACT_TITLE);
        customerOrder.setContactFirstName(UPDATED_CONTACT_FIRST_NAME);
        customerOrder.setContactSurname(UPDATED_CONTACT_SURNAME);
        customerOrder.setTel(UPDATED_TEL);
        customerOrder.setMob(UPDATED_MOB);
        customerOrder.setEmail(UPDATED_EMAIL);
        customerOrder.setAddress1(UPDATED_ADDRESS1);
        customerOrder.setAddress2(UPDATED_ADDRESS2);
        customerOrder.setAddress3(UPDATED_ADDRESS3);
        customerOrder.setTown(UPDATED_TOWN);
        customerOrder.setPostCode(UPDATED_POST_CODE);
        customerOrder.setProducts(UPDATED_PRODUCTS);
        customerOrder.setInterested(UPDATED_INTERESTED);
        customerOrder.setPaid(UPDATED_PAID);
        customerOrder.setNotes(UPDATED_NOTES);

        restCustomerOrderMockMvc.perform(put("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrder)))
                .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrders.get(customerOrders.size() - 1);
        assertThat(testCustomerOrder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerOrder.getContactTitle()).isEqualTo(UPDATED_CONTACT_TITLE);
        assertThat(testCustomerOrder.getContactFirstName()).isEqualTo(UPDATED_CONTACT_FIRST_NAME);
        assertThat(testCustomerOrder.getContactSurname()).isEqualTo(UPDATED_CONTACT_SURNAME);
        assertThat(testCustomerOrder.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCustomerOrder.getMob()).isEqualTo(UPDATED_MOB);
        assertThat(testCustomerOrder.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerOrder.getAddress1()).isEqualTo(UPDATED_ADDRESS1);
        assertThat(testCustomerOrder.getAddress2()).isEqualTo(UPDATED_ADDRESS2);
        assertThat(testCustomerOrder.getAddress3()).isEqualTo(UPDATED_ADDRESS3);
        assertThat(testCustomerOrder.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testCustomerOrder.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCustomerOrder.getProducts()).isEqualTo(UPDATED_PRODUCTS);
        assertThat(testCustomerOrder.getInterested()).isEqualTo(UPDATED_INTERESTED);
        assertThat(testCustomerOrder.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deleteCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

		int databaseSizeBeforeDelete = customerOrderRepository.findAll().size();

        // Get the customerOrder
        restCustomerOrderMockMvc.perform(delete("/api/customerOrders/{id}", customerOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
