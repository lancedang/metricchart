package com.lance.chart.dao;

import com.lance.chart.entity.CPUEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface CpuDAO extends CrudRepository<CPUEntity, Long>{

    @Query("select a from CPUEntity a where a.createDate > ?1 and a.createDate < ?2")
    List<CPUEntity> findCPUEntityByDate(Date start, Date end);

}
