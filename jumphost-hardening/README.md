# jumphost hardening (In Progress)

An example app with a bastion in a public subnet and a webserver also in public subnet

Creates a ssh_config with the parameters defined. You can then use it with ssh -F $SSH_CONFIG_FILE jumphost

The bastion is secured by multiple things:
1. allowing ssh access only to a defined list of ip adresses
2. Only allow ssh to secure hosts from the jumphost
3. Change ssh port on jumphost to the port defined
4. Add and configure endlessh on Port 22 to tricker Hackers


It also installs google-authenticator.
Since google-authenticator is interactive you need to log into the server and run:
<code>google-authenticator -t</code>
Insert the secret key to your google authenticator.
Insert y on update file.
Insert y on disallow multiples uses
Insert n on permit time skey
Insert y on enable rate-limiting

Then configure the pam file with:

<code>sudo sh -c  "echo 'auth required pam_google_authenticator.so nullok' >> /etc/pam.d/sshd"</code>
Configure the ssh file with these two commands:<br/>
<code>sudo sed -i "s/#ChallengeResponseAuthentication yes/ChallengeResponseAuthentication yes/g" /etc/ssh/sshd_config</code><br/>
<code>sudo sed -i "s/ChallengeResponseAuthentication no/#ChallengeResponseAuthentication no/g" /etc/ssh/sshd_config</code><br/>


Making ssh aware of mfa with:
<code>sudo sh -c  "echo 'AuthenticationMethods publickey,password publickey,keyboard-interactive' >> /etc/ssh/sshd_config"</code><br/>

Configure pam to use verification:

<code>sudo sed -i "s/auth.*substack.*password-auth/#auth substack password-auth/g" /etc/pam.d/sshd</code>

Then you can restart the sshd service:
<code>sudo systemctl restart sshd.service</code>

Also see https://www.digitalocean.com/community/tutorials/how-to-set-up-multi-factor-authentication-for-ssh-on-centos-7