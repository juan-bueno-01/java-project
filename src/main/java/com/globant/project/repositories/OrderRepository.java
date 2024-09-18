package com.globant.project.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.globant.project.domain.entities.OrderEntity;

/**
 * DeliveryRepository
 */
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query(value = "SELECT p.fantasyName, SUM(o.quantity) as totalSales, SUM(o.quantity * p.price) as totalIncome "
            + "FROM OrderEntity o JOIN o.productUuid p "
            + "WHERE o.createdAt BETWEEN :startDate AND :endDate "
            + "GROUP BY p.fantasyName")
    List<List<Object>> findProductSalesReportByDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT p.fantasyName, SUM(o.quantity) as totalSales, SUM(o.quantity * p.price) as totalIncome "
            + "FROM OrderEntity o JOIN o.productUuid p "
            + "WHERE o.createdAt BETWEEN :startDate AND :endDate "
            + "GROUP BY p.fantasyName "
            + "HAVING SUM(o.quantity) = (SELECT MIN(totalSales) "
            + "FROM (SELECT SUM(o2.quantity) as totalSales "
            + "FROM OrderEntity o2 "
            + "WHERE o2.createdAt BETWEEN :startDate AND :endDate "
            + "GROUP BY o2.productUuid) as salesSubquery)")
    List<List<Object>> findLeastSoldProductsByDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT p.fantasyName, SUM(o.quantity) as totalSales, SUM(o.quantity * p.price) as totalIncome "
            + "FROM OrderEntity o JOIN o.productUuid p "
            + "WHERE o.createdAt BETWEEN :startDate AND :endDate "
            + "GROUP BY p.fantasyName "
            + "HAVING SUM(o.quantity) = (SELECT MAX(totalSales) "
            + "FROM (SELECT SUM(o2.quantity) as totalSales "
            + "FROM OrderEntity o2 "
            + "WHERE o2.createdAt BETWEEN :startDate AND :endDate "
            + "GROUP BY o2.productUuid) as salesSubquery)")
    List<List<Object>> findMostSoldProductByDates(LocalDateTime startDate, LocalDateTime endDate);

}
