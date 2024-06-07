# PROVIDERS
# --------------------
terraform {
    required_providers {
        confluent = {
            source = "confluentinc/confluent"
            version = "1.76.0"
        }
    }
}
# RANDOM IDS
# --------------------
resource "random_id" "confluent" {
    byte_length = 3
}
# VARS
# --------------------
variable "example_name" {
    type = string
}
variable "aws_region" {
    type = string
    default = "us-east-2"
}
# ENV
# --------------------
resource "confluent_environment" "main" {
    display_name = var.example_name
}
# TF MANAGER
# --------------------
resource "confluent_service_account" "app_manager" {
    display_name = "app-manager-${random_id.confluent.hex}"
    description = "app-manager for '${var.example_name}'"
}
resource "confluent_role_binding" "app_manager_env_admin" {
    principal = "User:${confluent_service_account.app_manager.id}"
    role_name = "EnvironmentAdmin"
    crn_pattern = confluent_environment.main.resource_name
}
resource "confluent_api_key" "app_manager_sr" {
    display_name = "app-manager-sr-${random_id.confluent.hex}"
    description = "app-manager-sr-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.app_manager.id
        api_version = confluent_service_account.app_manager.api_version
        kind = confluent_service_account.app_manager.kind
    }
    managed_resource {
        id = confluent_schema_registry_cluster.main.id
        api_version = confluent_schema_registry_cluster.main.api_version
        kind = confluent_schema_registry_cluster.main.kind
        environment {
            id = confluent_environment.main.id
        }
    }
    depends_on = [
        confluent_service_account.app_manager,
        confluent_role_binding.app_manager_env_admin
    ]
}
resource "confluent_api_key" "app_manager_kafka" {
    display_name = "app-manager-kafka-${random_id.confluent.hex}"
    description = "app-manager-kafka-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.app_manager.id
        api_version = confluent_service_account.app_manager.api_version
        kind = confluent_service_account.app_manager.kind
    }
    managed_resource {
        id = confluent_kafka_cluster.main.id
        api_version = confluent_kafka_cluster.main.api_version
        kind = confluent_kafka_cluster.main.kind
        environment {
            id = confluent_environment.main.id
        }
    }
    depends_on = [
        confluent_service_account.app_manager,
        confluent_role_binding.app_manager_env_admin
    ]
}
# SCHEMA REGISTRY
# --------------------
data "confluent_schema_registry_region" "main" {
    cloud = "AWS"
    region = var.aws_region
    package = "ADVANCED"
}
resource "confluent_schema_registry_cluster" "main" {
    package = data.confluent_schema_registry_region.main.package
    environment {
        id = confluent_environment.main.id
    }
    region {
        id = data.confluent_schema_registry_region.main.id
    }
}
resource "confluent_schema_registry_cluster_config" "main" {
    schema_registry_cluster {
        id = confluent_schema_registry_cluster.main.id 
    }
    rest_endpoint = confluent_schema_registry_cluster.main.rest_endpoint
    // None by default. Examples can set this for their own subjects.
    compatibility_level = "NONE"
    credentials {
        key = confluent_api_key.app_manager_sr.id
        secret = confluent_api_key.app_manager_sr.secret
    }
}
resource "confluent_schema_registry_cluster_mode" "main" {
    schema_registry_cluster {
        id = confluent_schema_registry_cluster.main.id
    }
    rest_endpoint = confluent_schema_registry_cluster.main.rest_endpoint
    mode = "READWRITE"
    credentials {
        key = confluent_api_key.app_manager_sr.id 
        secret = confluent_api_key.app_manager_sr.secret
    }
}
# KAFKA
# --------------------
resource "confluent_kafka_cluster" "main" {
    display_name = "kafka-${var.example_name}"
    availability = "SINGLE_ZONE"
    cloud = "AWS"
    region = var.aws_region
    basic {}
    environment {
        id = confluent_environment.main.id 
    }
}
# KAFKA CLIENTS
# --------------------
resource "confluent_service_account" "clients" {
    display_name = "clients-${random_id.confluent.hex}"
    description = "Service account for clients"
}
resource "confluent_role_binding" "clients_sr" {
    principal = "User:${confluent_service_account.clients.id}"
    role_name = "ResourceOwner"
    crn_pattern = format("%s/%s", confluent_schema_registry_cluster.main.resource_name, "subject=*")
}
resource "confluent_role_binding" "clients_kafka" {
    principal = "User:${confluent_service_account.clients.id}"
    role_name = "CloudClusterAdmin"
    crn_pattern = confluent_kafka_cluster.main.rbac_crn
}
resource "confluent_api_key" "clients_sr" {
    display_name = "clients-sr-${random_id.confluent.hex}"
    description = "clients-sr-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.clients.id
        api_version = confluent_service_account.clients.api_version
        kind = confluent_service_account.clients.kind
    }
    managed_resource {
        id = confluent_schema_registry_cluster.main.id 
        api_version = confluent_schema_registry_cluster.main.api_version
        kind = confluent_schema_registry_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    depends_on = [
        confluent_role_binding.clients_sr
    ]
}
resource "confluent_api_key" "clients_kafka" {
    display_name = "clients-kafka-${random_id.confluent.hex}"
    description = "clients-kafka-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.clients.id
        api_version = confluent_service_account.clients.api_version
        kind = confluent_service_account.clients.kind
    }
    managed_resource {
        id = confluent_kafka_cluster.main.id
        api_version = confluent_kafka_cluster.main.api_version
        kind = confluent_kafka_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    depends_on = [
        confluent_role_binding.clients_kafka
    ]
}
# TOPICS
# --------------------
resource "confluent_kafka_topic" "sandwhiches" {
    topic_name = "pbj-sandwhiches"
    rest_endpoint = confluent_kafka_cluster.main.rest_endpoint
    credentials {
        key = confluent_api_key.app_manager_kafka.id
        secret = confluent_api_key.app_manager_kafka.secret
    }
    kafka_cluster {
        id = confluent_kafka_cluster.main.id
    }
}
# CLIENT PROPERTIES FILE
# --------------------
resource "local_file" "client_properties" {
    filename = "../reference-schemas/client.properties"
    content = <<-EOF
    ## Confluent Cloud Cluster
    bootstrap.servers=${substr(confluent_kafka_cluster.main.bootstrap_endpoint,11,-1)}
    security.protocol=SASL_SSL
    sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule   required username='${confluent_api_key.clients_kafka.id}'   password='${confluent_api_key.clients_kafka.secret}';
    sasl.mechanism=PLAIN
    # Confluent Cloud Schema Registry
    schema.registry.url=${confluent_schema_registry_cluster.main.rest_endpoint}
    basic.auth.credentials.source=USER_INFO
    basic.auth.user.info=${confluent_api_key.app_manager_sr.id}:${confluent_api_key.app_manager_sr.secret}
    # Required for correctness in Apache Kafka clients prior to 2.6
    client.dns.lookup=use_all_dns_ips
    # Best practice for higher availability in Apache Kafka clients prior to 3.0
    session.timeout.ms=45000
    EOF
}