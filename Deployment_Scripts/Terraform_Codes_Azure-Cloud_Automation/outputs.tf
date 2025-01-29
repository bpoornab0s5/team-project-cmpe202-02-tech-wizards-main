output "mysql_server_name" {
  description = "The name of the MySQL server"
  value       = azurerm_mysql_flexible_server.default.name
}

output "mysql_fully_qualified_domain_name" {
  description = "The fully qualified domain name of the MySQL server"
  value       = azurerm_mysql_flexible_server.default.fqdn
}

output "mysql_admin_username" {
  description = "The admin username for the MySQL server"
  value       = azurerm_mysql_flexible_server.default.administrator_login
}

output "mysql_admin_password" {
  description = "The admin password for the MySQL server"
  value       = random_password.password.result
  sensitive   = true
}

output "mysql_connection_string" {
  description = "The connection string to connect to the MySQL server"
  value = "mysql://${azurerm_mysql_flexible_server.default.administrator_login}:${random_password.password.result}@${azurerm_mysql_flexible_server.default.fqdn}:3306"
  sensitive   = true
}
