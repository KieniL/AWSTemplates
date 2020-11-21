#/bin/sh

sudo apt-get install -y unzip

sudo wget https://releases.hashicorp.com/terraform/0.13.5/terraform_0.13.5_linux_amd64.zip

sudo unzip terraform_0.13.5_linux_amd64.zip

sudo mv terraform /usr/local/bin
