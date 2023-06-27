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