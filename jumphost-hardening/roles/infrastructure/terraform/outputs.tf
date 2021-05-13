### The Ansible inventory file
resource "local_file" "SSH_Config" {
 content = templatefile("config.tmpl",
 {
  jumphost_dns = aws_instance.bastion.public_dns,
  securehost_ip = aws_instance.webserver.private_dns
 }
 )
 filename = "${var.ssh_config_path}/${var.ssh_config_name}"
}