# Confluent Cloud UI Proxy

When you privately network a Confluent Cloud Kafka cluster, some resources in the data plane become inaccessible from a web browser. This is because the cluster REST Endpoint will now resolve to a private IP that's only reachable from the VPC networked to the cluster. In order to still use the UI and CLI for these data plane resource, it's possible to configure a Nginx forward proxy to route the requests to the cluster from within the privately networked VPC. 

This simple Terraform module can be used to add and configure the Nginx forward proxy to the VPC. This setup is documented [here](https://docs.confluent.io/cloud/current/networking/ccloud-console-access.html) and this module follows the example shown in that documentation. 

## Important things

#### How to use this
The intended use of this Terraform configuration is as a module. It's more than likely that if you have the problem that makes this proxy relevant that you already have a VPC, Confluent Cloud Network, Cluster, etc. So, just adding this to configuration as a module could be a "simple fix". 

In order to add it, reference this folder in git as the source link, or better yet, clone this repo and copy it out and put the configs somewhere for use. Once you have that, add your module definition to your existing Terraform configuration, and add values for the following variables. 

```hcl
variable "aws_region" {
    type = string
}
variable "vpc_id" {
    type = string
}
variable "subnet_id" {
    type = string
}
# VPC Route Table configurations
variable "route_table_id" {
    type = string
    description = "The ID of an existing Route Table."
}
variable "create_routes" {
    type = bool
    default = true
    description = "Whether or not to create a route pointing to the Confluent Cloud cluster."
}
# Confluent Cloud Network configurations
variable "ccn_cidr" {
    type = string
}
# Confluent Cloud Cluster configurations
variable "cluster_bootstrap_server" {
    type = string
}
# Private Networking specification configurations
variable "use_tgw" {
    type = bool
    default = false
    description = "Whether or not TGW is the networking in use."
}
variable "tgw_id" {
    type = string
    default = null
}
variable "use_peering" {
    type = bool
    default = false
    description = "Whether or not VPC Peering is the networking in use."
}
variable "peering_connection_id" {
    type = string
    default = null
}
# AWS Instance shape for Nginx Proxy
variable "instance_type" {
    type = string
    default = "t2.small"
}
variable "naming_prefix" {
    type = string
    default = "tf-cc-ui-proxy-module-"
}
# Instance Security Group configurations
variable "create_security_group" {
    type = bool
    default = false
    description = "Whether or not to create a Security Group for the instance. Rules will be created for either an auto-created group, or an existing one. with 'security_group_id'."
}
variable "security_group_id" {
    type = string
    default = null
    description = "The ID of an existing Security Group. If blank, one will be created."
}
variable "whitelist_current_ip" {
    type = bool
    default = true
}
variable "whitelist_ec2_instance_connect" {
    type = bool
    default = true
}
```
#### Module outputs
The Terraform configuration has some outputs. These outputs are meant to be added to you `/etc/hosts` file. The proxy ultimately isn't much use if your computer doesn't know to send requests to the proxy server that were originally meant for the cluster endpoint. If you need more detail on this or want to know other approaches, check out the blog in the overview section above. 

#### Please note
You should test this out and make it your own before ever using it in production, or something like that. This is meant to be an example and get things started quickly if your new to Confluent Cloud. 

#### Conclusion
Use this Terraform module if you need to quickly configure a forward proxy for the Confluent Cloud UI!
