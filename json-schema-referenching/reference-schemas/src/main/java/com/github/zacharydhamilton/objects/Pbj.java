
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
 * PBJ
 * <p>
 * com.github.zacharydhamilton.events
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bread",
    "peanutbutter",
    "jelly"
})
public class Pbj {

    /**
     * Type of bread.
     * (Required)
     * 
     */
    @JsonProperty("bread")
    @JsonPropertyDescription("Type of bread.")
    private String bread;
    /**
     * PeanutButter
     * <p>
     * com.github.zacharydhamilton.events
     * (Required)
     * 
     */
    @JsonProperty("peanutbutter")
    @JsonPropertyDescription("com.github.zacharydhamilton.events")
    private PeanutButter peanutbutter;
    /**
     * Jelly
     * <p>
     * com.github.zacharydhamilton.events
     * (Required)
     * 
     */
    @JsonProperty("jelly")
    @JsonPropertyDescription("com.github.zacharydhamilton.events")
    private Jelly jelly;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Type of bread.
     * (Required)
     * 
     */
    @JsonProperty("bread")
    public String getBread() {
        return bread;
    }

    /**
     * Type of bread.
     * (Required)
     * 
     */
    @JsonProperty("bread")
    public void setBread(String bread) {
        this.bread = bread;
    }

    /**
     * PeanutButter
     * <p>
     * com.github.zacharydhamilton.events
     * (Required)
     * 
     */
    @JsonProperty("peanutbutter")
    public PeanutButter getPeanutbutter() {
        return peanutbutter;
    }

    /**
     * PeanutButter
     * <p>
     * com.github.zacharydhamilton.events
     * (Required)
     * 
     */
    @JsonProperty("peanutbutter")
    public void setPeanutbutter(PeanutButter peanutbutter) {
        this.peanutbutter = peanutbutter;
    }

    /**
     * Jelly
     * <p>
     * com.github.zacharydhamilton.events
     * (Required)
     * 
     */
    @JsonProperty("jelly")
    public Jelly getJelly() {
        return jelly;
    }

    /**
     * Jelly
     * <p>
     * com.github.zacharydhamilton.events
     * (Required)
     * 
     */
    @JsonProperty("jelly")
    public void setJelly(Jelly jelly) {
        this.jelly = jelly;
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
        sb.append(Pbj.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("bread");
        sb.append('=');
        sb.append(((this.bread == null)?"<null>":this.bread));
        sb.append(',');
        sb.append("peanutbutter");
        sb.append('=');
        sb.append(((this.peanutbutter == null)?"<null>":this.peanutbutter));
        sb.append(',');
        sb.append("jelly");
        sb.append('=');
        sb.append(((this.jelly == null)?"<null>":this.jelly));
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
        result = ((result* 31)+((this.bread == null)? 0 :this.bread.hashCode()));
        result = ((result* 31)+((this.jelly == null)? 0 :this.jelly.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.peanutbutter == null)? 0 :this.peanutbutter.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Pbj) == false) {
            return false;
        }
        Pbj rhs = ((Pbj) other);
        return (((((this.bread == rhs.bread)||((this.bread!= null)&&this.bread.equals(rhs.bread)))&&((this.jelly == rhs.jelly)||((this.jelly!= null)&&this.jelly.equals(rhs.jelly))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.peanutbutter == rhs.peanutbutter)||((this.peanutbutter!= null)&&this.peanutbutter.equals(rhs.peanutbutter))));
    }

}
