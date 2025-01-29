# Generate random resource group name
resource "random_pet" "rg_name" {
  prefix = var.resource_group_name_prefix
}

# Create Resource Group
resource "azurerm_resource_group" "rg" {
  location = var.resource_group_location
  name     = random_pet.rg_name.id
}

# Generate random string for name
resource "random_string" "name" {
  length  = 8
  lower   = true
  numeric = false
  special = false
  upper   = false
}

# Generate random password
resource "random_password" "password" {
  length           = 12
  lower            = true
  min_lower        = 2
  min_numeric      = 2
  min_special      = 2
  min_upper        = 2
  override_special = "_-"
}

# MySQL Flexible Server resource
resource "azurerm_mysql_flexible_server" "default" {
   location               = "centralus"
   name                   = "mysql007yrt-${random_string.name.result}"
   resource_group_name    = azurerm_resource_group.rg.name
   administrator_login    = "adminuser"
   administrator_password = "AIzaSyAR89P22O8kFuEl2P_fNDtqMS9aWPD3HHk@deepak"

   # Recommended SKU for General Purpose
   sku_name = "B_Standard_B1ms"
   
   # Updated to a more recent version
   version= "8.0.21"

   storage {
     size_gb = 20
   }

   backup_retention_days         = 7
   geo_redundant_backup_enabled  = false
}

# Allow public access from all IPs
resource "azurerm_mysql_flexible_server_firewall_rule" "allow_all" {
  name                = "allow-all"
  resource_group_name = azurerm_resource_group.rg.name
  server_name         = azurerm_mysql_flexible_server.default.name
  start_ip_address    = "0.0.0.0"
  end_ip_address      = "255.255.255.255"
}
