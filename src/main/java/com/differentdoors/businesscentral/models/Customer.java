package com.differentdoors.businesscentral.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Customer {
    @JsonProperty("@odata.etag")
    private String etag;

    private String no;
    private String name;
    private String address;
    private String countryRegionCode;
    private String city;
    private String postCode;
    private String phoneNo;
    private String eMail;
    private String genBusPostingGroup;
    private String vatBusPostingGroup;
    private String customerPostingGroup;
    private boolean allowLineDisc;
    private String paymentTermsCode;
    private String taxAreaID;
    private boolean taxLiable;

    public void setNo(String no) {
        this.no = "K" + no;
    }
}
