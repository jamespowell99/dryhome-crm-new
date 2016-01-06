package uk.co.dryhome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "customer_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customerorder")
public class CustomerOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "dispatch_date")
    private LocalDate dispatchDate;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "placed_by")
    private String placedBy;

    @Column(name = "method")
    private String method;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "invoice_notes1")
    private String invoiceNotes1;

    @Column(name = "invoice_notes2")
    private String invoiceNotes2;

    @Column(name = "notes")
    private String notes;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payyment_amount")
    private String payymentAmount;

    @NotNull
    @Column(name = "vat_rate", precision=10, scale=2, nullable = false)
    private BigDecimal vatRate;

    @Column(name = "n")
    private String n;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "customerOrder")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderItem> itemss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(String placedBy) {
        this.placedBy = placedBy;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNotes1() {
        return invoiceNotes1;
    }

    public void setInvoiceNotes1(String invoiceNotes1) {
        this.invoiceNotes1 = invoiceNotes1;
    }

    public String getInvoiceNotes2() {
        return invoiceNotes2;
    }

    public void setInvoiceNotes2(String invoiceNotes2) {
        this.invoiceNotes2 = invoiceNotes2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayymentAmount() {
        return payymentAmount;
    }

    public void setPayymentAmount(String payymentAmount) {
        this.payymentAmount = payymentAmount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<OrderItem> getItemss() {
        return itemss;
    }

    public void setItemss(Set<OrderItem> orderItems) {
        this.itemss = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerOrder customerOrder = (CustomerOrder) o;
        return Objects.equals(id, customerOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            ", orderDate='" + orderDate + "'" +
            ", dispatchDate='" + dispatchDate + "'" +
            ", invoiceDate='" + invoiceDate + "'" +
            ", placedBy='" + placedBy + "'" +
            ", method='" + method + "'" +
            ", invoiceNumber='" + invoiceNumber + "'" +
            ", invoiceNotes1='" + invoiceNotes1 + "'" +
            ", invoiceNotes2='" + invoiceNotes2 + "'" +
            ", notes='" + notes + "'" +
            ", paymentDate='" + paymentDate + "'" +
            ", paymentStatus='" + paymentStatus + "'" +
            ", paymentType='" + paymentType + "'" +
            ", payymentAmount='" + payymentAmount + "'" +
            ", vatRate='" + vatRate + "'" +
            ", n='" + n + "'" +
            '}';
    }
}
