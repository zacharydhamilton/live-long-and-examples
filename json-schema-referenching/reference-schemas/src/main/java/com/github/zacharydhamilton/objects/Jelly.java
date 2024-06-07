
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
 * Jelly
 * <p>
 * com.github.zacharydhamilton.events
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "flavor",
    "organic"
})
public class Jelly {

    /**
     * The flavor of this Jelly (i.e. grape, strawberry, raspberry).
     * (Required)
     * 
     */
    @JsonProperty("flavor")
    @JsonPropertyDescription("The flavor of this Jelly (i.e. grape, strawberry, raspberry).")
    private String flavor;
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
     * The flavor of this Jelly (i.e. grape, strawberry, raspberry).
     * (Required)
     * 
     */
    @JsonProperty("flavor")
    public String getFlavor() {
        return flavor;
    }

    /**
     * The flavor of this Jelly (i.e. grape, strawberry, raspberry).
     * (Required)
     * 
     */
    @JsonProperty("flavor")
    public void setFlavor(String flavor) {
        this.flavor = flavor;
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
        sb.append(Jelly.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("flavor");
        sb.append('=');
        sb.append(((this.flavor == null)?"<null>":this.flavor));
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
        result = ((result* 31)+((this.flavor == null)? 0 :this.flavor.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.organic == null)? 0 :this.organic.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jelly) == false) {
            return false;
        }
        Jelly rhs = ((Jelly) other);
        return ((((this.flavor == rhs.flavor)||((this.flavor!= null)&&this.flavor.equals(rhs.flavor)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.organic == rhs.organic)||((this.organic!= null)&&this.organic.equals(rhs.organic))));
    }

}
