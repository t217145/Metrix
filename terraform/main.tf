resource "azurerm_resource_group" "comps368tma04" {
  name     = "comps368-tma04"
  location = "East Asia"
}

resource "azurerm_mssql_server" "comps368tma04db" {
  name                         = "${var.student_id}-comps368-tma04"
  resource_group_name          = azurerm_resource_group.comps368tma04.name
  location                     = azurerm_resource_group.comps368tma04.location
  version                      = "12.0"
  administrator_login          = var.database_server_login
  administrator_login_password = var.database_server_password
}

resource "azurerm_mssql_database" "comps368tma04db" {
  name           = "comps368tma04db"
  server_id      = azurerm_mssql_server.comps368tma04db.id
  collation      = "SQL_Latin1_General_CP1_CI_AS"
  license_type   = "LicenseIncluded"
  max_size_gb    = 1
  read_scale     = false
  sku_name       = "Basic"
  zone_redundant = false
  storage_account_type = "LRS"
}

resource "azurerm_mssql_firewall_rule" "allNetwork" {
  name             = "AllNetwork"
  server_id        = azurerm_mssql_server.comps368tma04db.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}

resource "azurerm_container_registry" "acr" {
  name                = "${var.student_id}comps368tma04"
  resource_group_name = azurerm_resource_group.comps368tma04.name
  location            = azurerm_resource_group.comps368tma04.location
  sku                 = "Standard"
  admin_enabled       = true
}

resource "azurerm_kubernetes_cluster" "comps368aks" {
  name                = "${var.student_id}-comps368-tma04"
  resource_group_name = azurerm_resource_group.comps368tma04.name
  location            = azurerm_resource_group.comps368tma04.location
  dns_prefix          = "${var.student_id}-comps368-tma04-dns"

  default_node_pool {
    name       = "agentpool"
	node_count = 1
    vm_size    = "Standard_B2s"
  }
  
  identity {
    type = "SystemAssigned"
  }  
}

resource "azurerm_role_assignment" "comps368aksacr" {
  principal_id                     = azurerm_kubernetes_cluster.comps368aks.kubelet_identity[0].object_id
  role_definition_name             = "AcrPull"
  scope                            = azurerm_container_registry.acr.id
  skip_service_principal_aad_check = true
}