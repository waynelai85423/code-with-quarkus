package org.youbike.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouBikeDto {

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
