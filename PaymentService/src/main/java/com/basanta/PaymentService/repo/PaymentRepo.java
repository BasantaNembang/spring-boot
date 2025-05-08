package com.basanta.PaymentService.repo;


import com.basanta.PaymentService.entity.PaymentDetails;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Observed
public interface PaymentRepo extends JpaRepository<PaymentDetails, String> {

    @Query(
            value = "SELECT * FROM payment_details u WHERE u.order_id =:n",
            nativeQuery = true)
    PaymentDetails findByOrderId(@Param("n") String id);




}
