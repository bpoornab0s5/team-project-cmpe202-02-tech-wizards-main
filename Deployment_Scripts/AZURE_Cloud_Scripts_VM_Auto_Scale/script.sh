#!/bin/bash

sudo su


# Update system packages
sudo apt update -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Install Maven
sudo apt install maven -y

# Create a directory for the application
mkdir -p /home/azureuser/app
sudo chown azureuser:azureuser /home/azureuser/app

# Download the updated JAR file
wget -O /home/azureuser/app/restaurantfinder-0.0.1-SNAPSHOT.jar https://myrestaurantstorage.blob.core.windows.net/backend/restaurantfinder-0.0.1-SNAPSHOT.jar

# Create systemd service for the application
sudo tee /etc/systemd/system/restaurantfinder.service > /dev/null << EOF
[Unit]
Description=Spring Boot Application
After=network.target

[Service]
User=azureuser
ExecStart=/usr/bin/java -jar /home/azureuser/app/restaurantfinder-0.0.1-SNAPSHOT.jar
Restart=always

[Install]
WantedBy=multi-user.target
EOF

# Reload systemd to recognize the new service
sudo systemctl daemon-reload

# Enable and start the service
sudo systemctl enable restaurantfinder
sudo systemctl start restaurantfinder