
package com.github.zacharydhamilton.objects;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * PeanutButter
 * <p>
 * com.github.zacharydhamilton.events
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "smooth",
    "organic"
})
public class PeanutButter {

    /**
     * Is this smooth or not. Not implies chunky
     * (Required)
     * 
     */
    @JsonProperty("smooth")
    @JsonPropertyDescription("Is this smooth or not. Not implies chunky")
    private Boolean smooth;
    /**
     * Is this organic or not.
     * (Required)
     * 
     */
    @JsonProperty("organic")
    @JsonPropertyDescription("Is this organic or not.")
    private Boolean organic;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Is this smooth or not. Not implies chunky
     * (Required)
     * 
     */
    @JsonProperty("smooth")
    public Boolean getSmooth() {
        return smooth;
    }

    /**
     * Is this smooth or not. Not implies chunky
     * (Required)
     * 
     */
    @JsonProperty("smooth")
    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
    }

    /**
     * Is this organic or not.
     * (Required)
     * 
     */
    @JsonProperty("organic")
    public Boolean getOrganic() {
        return organic;
    }

    /**
     * Is this organic or not.
     * (Required)
     * 
     */
    @JsonProperty("organic")
    public void setOrganic(Boolean organic) {
        this.organic = organic;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PeanutButter.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("smooth");
        sb.append('=');
        sb.append(((this.smooth == null)?"<null>":this.smooth));
        sb.append(',');
        sb.append("organic");
        sb.append('=');
        sb.append(((this.organic == null)?"<null>":this.organic));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.organic == null)? 0 :this.organic.hashCode()));
        result = ((result* 31)+((this.smooth == null)? 0 :this.smooth.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PeanutButter) == false) {
            return false;
        }
        PeanutButter rhs = ((PeanutButter) other);
        return ((((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties)))&&((this.organic == rhs.organic)||((this.organic!= null)&&this.organic.equals(rhs.organic))))&&((this.smooth == rhs.smooth)||((this.smooth!= null)&&this.smooth.equals(rhs.smooth))));
    }

}
