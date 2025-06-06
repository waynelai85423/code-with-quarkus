package org.youbike.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class YouBike {

    @Id
    private String sno;
    private String sna;
    private String sarea;
    private String mday;
    private String ar;
    private String sareaen;
    private String snaen;
    private String aren;
    private String act;
    private String srcUpdateTime;
    private String updateTime;
    private String infoTime;
    private String infoDate;
    private Integer total;
    private Integer available_rent_bikes;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer available_return_bikes;

}
