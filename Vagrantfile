# -*- mode: ruby -*-
# vi set ft=ruby :

servers=[
    {
        :hostname => "AMIAWSArchTest",
        :ip => "192.168.100.10",
        :box => "bento/amazonlinux-2",
        :ram => 1024,
        :cpu => 2,
        :provision => 
            "sudo yum update -y \
            && sudo yum install -y git \
            && sudo mkdir /home/ansible \
            && sudo sh -c 'touch /home/ansible/ansible_hosts' \
            && sudo sh -c 'echo \"[localhost]\" > /home/ansible/ansible_hosts' \
            && sudo sh -c 'echo \"localhost  ansible_connection=local\" >> /home/ansible/ansible_hosts' \
            && sudo sh -c 'echo \"export ANSIBLE_INVENTORY=~/ansible_hosts\" >> /etc/profile' \
            && sudo amazon-linux-extras install epel -y \
            && sudo yum install ansible -y \
            && sudo git clone https://298fe6755e0453db49220cf6f33c78e25c2a4fd5@github.com/guisesterheim/LogServiceWithRedis/ \
            && sudo ansible-playbook LogServiceWithRedis/ansible_startup/site.yml"
    }
]

Vagrant.configure(2) do |config|

    servers.each do |machine|
        config.vm.define machine[:hostname] do |node|
            node.vm.box = machine[:box]
            node.vm.hostname = machine[:hostname]
            node.vm.provision "shell", inline: machine[:provision]
            node.vm.network "private_network", ip: machine[:ip]
            node.vm.provider "virtualbox" do |vb|
                vb.customize ["modifyvm", :id, "--memory", machine[:ram]]
            end
        end
    end
end