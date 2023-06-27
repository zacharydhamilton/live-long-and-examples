locals {
    kafka_cluster_endpoint = trimsuffix(substr(var.cluster_bootstrap_server, 11, -1), ":9092")
}
output "nginx_proxy_ip" {
    value = aws_eip.ui_proxy.public_ip
}
output "kafka_cluster_endpoint" {
    value = local.kafka_cluster_endpoint
}
output "etc_hosts_entry" {
    value = <<-EOF
    # Nginx proxy for Dedicated TGW Cluster
    ${aws_eip.ui_proxy.public_ip} ${local.kafka_cluster_endpoint}
    EOF
}