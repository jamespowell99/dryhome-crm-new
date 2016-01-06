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
import java.time.LocalDate;
import java.time.ZoneId;
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

    private static final String DEFAULT_ORDER_NUMBER = "AAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBB";

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DISPATCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISPATCH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PLACED_BY = "AAAAA";
    private static final String UPDATED_PLACED_BY = "BBBBB";
    private static final String DEFAULT_METHOD = "AAAAA";
    private static final String UPDATED_METHOD = "BBBBB";
    private static final String DEFAULT_INVOICE_NUMBER = "AAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBB";
    private static final String DEFAULT_INVOICE_NOTES1 = "AAAAA";
    private static final String UPDATED_INVOICE_NOTES1 = "BBBBB";
    private static final String DEFAULT_INVOICE_NOTES2 = "AAAAA";
    private static final String UPDATED_INVOICE_NOTES2 = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PAYMENT_STATUS = "AAAAA";
    private static final String UPDATED_PAYMENT_STATUS = "BBBBB";
    private static final String DEFAULT_PAYMENT_TYPE = "AAAAA";
    private static final String UPDATED_PAYMENT_TYPE = "BBBBB";
    private static final String DEFAULT_PAYYMENT_AMOUNT = "AAAAA";
    private static final String UPDATED_PAYYMENT_AMOUNT = "BBBBB";

    private static final BigDecimal DEFAULT_VAT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VAT_RATE = new BigDecimal(2);
    private static final String DEFAULT_N = "AAAAA";
    private static final String UPDATED_N = "BBBBB";

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
        customerOrder.setOrderNumber(DEFAULT_ORDER_NUMBER);
        customerOrder.setOrderDate(DEFAULT_ORDER_DATE);
        customerOrder.setDispatchDate(DEFAULT_DISPATCH_DATE);
        customerOrder.setInvoiceDate(DEFAULT_INVOICE_DATE);
        customerOrder.setPlacedBy(DEFAULT_PLACED_BY);
        customerOrder.setMethod(DEFAULT_METHOD);
        customerOrder.setInvoiceNumber(DEFAULT_INVOICE_NUMBER);
        customerOrder.setInvoiceNotes1(DEFAULT_INVOICE_NOTES1);
        customerOrder.setInvoiceNotes2(DEFAULT_INVOICE_NOTES2);
        customerOrder.setNotes(DEFAULT_NOTES);
        customerOrder.setPaymentDate(DEFAULT_PAYMENT_DATE);
        customerOrder.setPaymentStatus(DEFAULT_PAYMENT_STATUS);
        customerOrder.setPaymentType(DEFAULT_PAYMENT_TYPE);
        customerOrder.setPayymentAmount(DEFAULT_PAYYMENT_AMOUNT);
        customerOrder.setVatRate(DEFAULT_VAT_RATE);
        customerOrder.setN(DEFAULT_N);
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
        assertThat(testCustomerOrder.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testCustomerOrder.getDispatchDate()).isEqualTo(DEFAULT_DISPATCH_DATE);
        assertThat(testCustomerOrder.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testCustomerOrder.getPlacedBy()).isEqualTo(DEFAULT_PLACED_BY);
        assertThat(testCustomerOrder.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testCustomerOrder.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testCustomerOrder.getInvoiceNotes1()).isEqualTo(DEFAULT_INVOICE_NOTES1);
        assertThat(testCustomerOrder.getInvoiceNotes2()).isEqualTo(DEFAULT_INVOICE_NOTES2);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomerOrder.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testCustomerOrder.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testCustomerOrder.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testCustomerOrder.getPayymentAmount()).isEqualTo(DEFAULT_PAYYMENT_AMOUNT);
        assertThat(testCustomerOrder.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testCustomerOrder.getN()).isEqualTo(DEFAULT_N);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setOrderNumber(null);

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
    public void checkOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setOrderDate(null);

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
    public void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerOrderRepository.findAll().size();
        // set the field null
        customerOrder.setVatRate(null);

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
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
                .andExpect(jsonPath("$.[*].dispatchDate").value(hasItem(DEFAULT_DISPATCH_DATE.toString())))
                .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
                .andExpect(jsonPath("$.[*].placedBy").value(hasItem(DEFAULT_PLACED_BY.toString())))
                .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
                .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].invoiceNotes1").value(hasItem(DEFAULT_INVOICE_NOTES1.toString())))
                .andExpect(jsonPath("$.[*].invoiceNotes2").value(hasItem(DEFAULT_INVOICE_NOTES2.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
                .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].payymentAmount").value(hasItem(DEFAULT_PAYYMENT_AMOUNT.toString())))
                .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.intValue())))
                .andExpect(jsonPath("$.[*].n").value(hasItem(DEFAULT_N.toString())));
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
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.dispatchDate").value(DEFAULT_DISPATCH_DATE.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.placedBy").value(DEFAULT_PLACED_BY.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.invoiceNotes1").value(DEFAULT_INVOICE_NOTES1.toString()))
            .andExpect(jsonPath("$.invoiceNotes2").value(DEFAULT_INVOICE_NOTES2.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.payymentAmount").value(DEFAULT_PAYYMENT_AMOUNT.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.intValue()))
            .andExpect(jsonPath("$.n").value(DEFAULT_N.toString()));
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
        customerOrder.setOrderNumber(UPDATED_ORDER_NUMBER);
        customerOrder.setOrderDate(UPDATED_ORDER_DATE);
        customerOrder.setDispatchDate(UPDATED_DISPATCH_DATE);
        customerOrder.setInvoiceDate(UPDATED_INVOICE_DATE);
        customerOrder.setPlacedBy(UPDATED_PLACED_BY);
        customerOrder.setMethod(UPDATED_METHOD);
        customerOrder.setInvoiceNumber(UPDATED_INVOICE_NUMBER);
        customerOrder.setInvoiceNotes1(UPDATED_INVOICE_NOTES1);
        customerOrder.setInvoiceNotes2(UPDATED_INVOICE_NOTES2);
        customerOrder.setNotes(UPDATED_NOTES);
        customerOrder.setPaymentDate(UPDATED_PAYMENT_DATE);
        customerOrder.setPaymentStatus(UPDATED_PAYMENT_STATUS);
        customerOrder.setPaymentType(UPDATED_PAYMENT_TYPE);
        customerOrder.setPayymentAmount(UPDATED_PAYYMENT_AMOUNT);
        customerOrder.setVatRate(UPDATED_VAT_RATE);
        customerOrder.setN(UPDATED_N);

        restCustomerOrderMockMvc.perform(put("/api/customerOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerOrder)))
                .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        assertThat(customerOrders).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrders.get(customerOrders.size() - 1);
        assertThat(testCustomerOrder.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testCustomerOrder.getDispatchDate()).isEqualTo(UPDATED_DISPATCH_DATE);
        assertThat(testCustomerOrder.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testCustomerOrder.getPlacedBy()).isEqualTo(UPDATED_PLACED_BY);
        assertThat(testCustomerOrder.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testCustomerOrder.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testCustomerOrder.getInvoiceNotes1()).isEqualTo(UPDATED_INVOICE_NOTES1);
        assertThat(testCustomerOrder.getInvoiceNotes2()).isEqualTo(UPDATED_INVOICE_NOTES2);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomerOrder.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testCustomerOrder.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testCustomerOrder.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testCustomerOrder.getPayymentAmount()).isEqualTo(UPDATED_PAYYMENT_AMOUNT);
        assertThat(testCustomerOrder.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testCustomerOrder.getN()).isEqualTo(UPDATED_N);
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
