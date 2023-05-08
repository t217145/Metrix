output "acrUrl" {
  value = azurerm_container_registry.acr.login_server
}

output "acrUsr" {
  value = azurerm_container_registry.acr.admin_username
}

output "acrPwd" {
  value = azurerm_container_registry.acr.admin_password
  sensitive = true
}