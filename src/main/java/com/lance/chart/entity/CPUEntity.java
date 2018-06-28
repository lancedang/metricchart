package com.lance.chart.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "app_test_cpu", schema = "charts")
public class CPUEntity implements Serializable {
    private static final long serialVersionUID = 5975291119115395011L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "cpu_percent")
    private Double cpuPercent;

    @Column(name = "create_date")
    private Date createDate;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    @Override
    public String toString() {
        return "CPUEntity [packageName=" + packageName + ", processId=" + processId + ", cpuPercent=" + cpuPercent
                + ", createDate=" + createDate + "]";
    }

    public Double getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(Double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

}
