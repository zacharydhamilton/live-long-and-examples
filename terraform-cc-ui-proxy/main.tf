// Cloudinit configuration for setting up nginx within an instance
data "cloudinit_config" "nginx" {
    gzip = false
    base64_encode = false
    part {
        content_type = "text/x-shellscript"
        filename = "configure.sh"
        content = <<-EOF
            #!/bin/sh
            sudo amazon-linux-extras install nginx1 -y
            sudo yum install nginx-mod-stream -y
        EOF
    }
    part {
        content_type = "text/cloud-config"
        filename = "cloud-config.yaml"
        content = <<-EOF
            #cloud-config
            ${jsonencode({
                write_files = [
                    {
                        path = "/etc/nginx/nginx.conf"
                        permissions = "0644"
                        content = <<-EOF
                            load_module '/usr/lib64/nginx/modules/ngx_stream_module.so';

                            events {}
                            stream {
                                map $ssl_preread_server_name $targetBackend {
                                    default $ssl_preread_server_name;
                                }
                                server {
                                    listen 443;
                                    proxy_connect_timeout 1s;
                                    proxy_timeout 7200s;
                                    # resolver 127.0.0.53; # default
                                    resolver 169.254.169.253; # for AWS
                                    # resolver 168.63.129.16;  # for Azure
                                    # resolver 169.254.169.254;  # for Google
                                    proxy_pass $targetBackend:443;
                                    ssl_preread on;
                                }

                                log_format stream_routing '[$time_local] remote address $remote_addr'
                                                'with SNI name "$ssl_preread_server_name" '
                                                'proxied to "$upstream_addr" '
                                                '$protocol $status $bytes_sent $bytes_received '
                                                '$session_time';
                                access_log /var/log/nginx/stream-access.log stream_routing;
                            }
                        EOF
                    }
                ]
            })}
        EOF
    }
    part {
        content_type = "text/x-shellscript"
        filename = "nginx-start.sh"
        content = <<-EOF
            #!/bin/sh
            systemctl start nginx
        EOF
    }
}
# Find instance ami and type
data "aws_ami" "amazon_linux" {
    owners = [ "amazon" ]
    most_recent = true
    filter {
        name = "name"
        values = [ "amzn2-ami-kernel-5.10-hvm-*" ]
    }
}
data "aws_ec2_instance_type" "ui_proxy" {
    instance_type = var.instance_type
}
// Instance to host nginx
resource "aws_instance" "ui_proxy" {
    ami = data.aws_ami.amazon_linux.id
    associate_public_ip_address = true
    instance_type = data.aws_ec2_instance_type.ui_proxy.instance_type
    subnet_id = var.subnet_id
    vpc_security_group_ids = [ "${var.create_security_group ? aws_security_group.ui_proxy[0].id : var.security_group_id}" ]
    user_data = data.cloudinit_config.nginx.rendered
    tags = {
        Name = "${var.naming_prefix}ui-proxy-instance"
    }
}
// Elastic IP to have a distinct address to add to /etc/hosts
resource "aws_eip" "ui_proxy" {
    instance = aws_instance.ui_proxy.id 
    tags = {
        Name = "${var.naming_prefix}ui-proxy-eip"
    }
}
# Capture the current public ip of the machine running this
data "http" "myip" {
    count = var.whitelist_current_ip ? 1 : 0
    url = "http://ipv4.icanhazip.com"
}
# Gather all the service ips from aws
data "http" "ec2_instance_connect" {
    count = var.whitelist_ec2_instance_connect ? 1 : 0
    url = "https://ip-ranges.amazonaws.com/ip-ranges.json"
}
# Specifically get the ec2 instance connect service ip so it can be whitelisted
locals {
    ec2_instance_connect_ip = [ for e in jsondecode(data.http.ec2_instance_connect[0].response_body)["prefixes"] : e.ip_prefix if e.region == "${var.aws_region}" && e.service == "EC2_INSTANCE_CONNECT" ]
}
// Security group to configure access to a restricted set of IPs
resource "aws_security_group" "ui_proxy" {
    count = var.create_security_group ? 1 : 0
    vpc_id = var.vpc_id
    name = "${var.naming_prefix}ui-proxy-instance-sg"
    egress {
        description = "Allow all outbound"
        from_port = 0
        to_port = 0 
        protocol = -1
        cidr_blocks = [ "0.0.0.0/0" ]
    }
    tags = {
        Name = "${var.naming_prefix}ui-proxy-instance-sg"
    }
}
resource "aws_security_group_rule" "my_current_ip_ssh" {
    count = var.whitelist_current_ip ? 1 : 0
    type = "ingress"
    from_port = 0
    to_port = 22
    protocol = "TCP"
    cidr_blocks = [ "${chomp(data.http.myip[0].response_body)}/32" ]
    security_group_id = var.create_security_group ? aws_security_group.ui_proxy[0].id : var.security_group_id
}
resource "aws_security_group_rule" "my_current_ip_https" {
    count = var.whitelist_current_ip ? 1 : 0
    type = "ingress"
    from_port = 0
    to_port = 443
    protocol = "TCP"
    cidr_blocks = [ "${chomp(data.http.myip[0].response_body)}/32" ]
    security_group_id = var.create_security_group ? aws_security_group.ui_proxy[0].id : var.security_group_id
}
resource "aws_security_group_rule" "ec2_instance_connect_ssh" {
    count = var.whitelist_ec2_instance_connect ? 1 : 0
    type = "ingress"
    from_port = 0
    to_port = 22
    protocol = "TCP"
    cidr_blocks = [ "${local.ec2_instance_connect_ip[0]}" ]
    security_group_id = var.create_security_group ? aws_security_group.ui_proxy[0].id : var.security_group_id
}

// Route to target the TGW when IPs resolve to CC
resource "aws_route" "proxy-tgw" {
    count = var.use_tgw && var.create_routes ? 1 : 0
    route_table_id = var.route_table_id
    destination_cidr_block = var.ccn_cidr
    transit_gateway_id = var.tgw_id
}
// Route to target the peering connection when IPs resolve to CC
resource "aws_route" "proxy-peering" {
    count = var.use_peering && var.create_routes ? 1 : 0
    route_table_id = var.route_table_id
    destination_cidr_block = var.ccn_cidr
    vpc_peering_connection_id = var.peering_connection_id
}
