variable "resource_group_location" {
  type        = string
  default     = "East US"
  description = "Location of the resource group."
}

variable "resource_group_name_prefix" {
  type        = string
  default     = "college-mysql"
  description = "Prefix for the resource group name."
}
