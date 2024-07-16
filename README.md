# Blog
## 技术选型
-  使用Java 17
- 使用Spring Boot 3.3.1
- 使用MySQL8作为数据库
- ORM框架使用mybatis-plus
- RESTful API设计
- 用redis+jwt实现基本的身份认证机制

## MySQL表结构设计
1.用户表
![image](https://github.com/user-attachments/assets/f3133e2a-bc5a-49f7-9755-988bf72ebf59)

2.文章表
![image](https://github.com/user-attachments/assets/f3dcb738-7289-4348-8233-1f2f28eb81b3)

####  **因为考虑到文本长度 content选用text不能使用varchar**
## 注册
1.注册时用户名不能重复而且有正则校验防止出现太离谱数据，密码模拟前端MD5传输后端接收到MD5后再次加盐MD5加密

![image](https://github.com/user-attachments/assets/7b7e3be3-ad72-45b6-88f8-6008e1aa9d4a)

2.注册使用验证码防止系统被恶意暴力注册，验证码通过redis存储并且控制过期时间
![image](https://github.com/user-attachments/assets/11a25bb9-f0f7-4e18-8779-19599c460413)

接口演示

![image](https://github.com/user-attachments/assets/5a5b5267-d0d3-40e2-8d08-d76d5efca623)

## 登录
登录和注册一样常规数据校验，然后验证码拦截
在登录成功后生成jwt令牌存放在前端和redis各存一份

![image](https://github.com/user-attachments/assets/0d28be75-ab34-4a0d-8c24-d8667002efe6)
接口演示
![image](https://github.com/user-attachments/assets/372ebdb7-32e4-4612-9d37-897a0d1bfcd2)



## 获取用户文章（权限校验）
1.设置一个拦截器把需要登录权限校验的请求拦截下来

![image](https://github.com/user-attachments/assets/d3b4e7c6-7b72-4a5b-92eb-4a61997c4b3e)

2.拦截后把请求中的uid和jwt获取下来，通过去redis中找到对应的key（uid）然后获取value（jwt），
然后进行对比如果jwt不相同，说明有一下情况，redis过期没有jwt，不同设备登录顶号，恶意破解。
由于jwt不能续签，所以我利用redis去控制jwt的过期时间，如果低于30分钟就执行redis重新设时间。

![image](https://github.com/user-attachments/assets/278096a8-4f42-4886-abd4-fddf09e04097)
例如这个接口uid和jwt跟redis对不上

![image](https://github.com/user-attachments/assets/28e6ed72-7258-468e-9de9-3d1556b39a29)

验证成功则接口正常执行

![image](https://github.com/user-attachments/assets/47c3d515-02a2-48c5-b336-7fd602fcb6d9)

## 对于文章的增删改
其实只需注意是否是当前用户的文章可以再接口加入这个请求头uid用于辨别，具体代码git仓库有，我就贴几张结果图

![image](https://github.com/user-attachments/assets/93ca356e-1474-4726-88c0-19572854afb2)
#### 1.新增

![image](https://github.com/user-attachments/assets/9c43391a-1984-46fb-bb5d-cca21ceab102)
#### 2.修改

![image](https://github.com/user-attachments/assets/45845949-a9e3-4509-9ea2-fc3796045f4b)

![image](https://github.com/user-attachments/assets/91afb8cf-a44c-42b6-b5de-225c30c0f1d4)

#### 3.删除

![image](https://github.com/user-attachments/assets/a2d0b922-3a0d-473d-9976-2caab893b89d)
## 部署思路
### 1.开发防火墙
systemctl start firewalld.service
开发端口
firewall-cmd --zone=public --add-port=6379/tcp --permanent
firewall-cmd --zone=public --add-port=8000/tcp --permanent
firewall-cmd --zone=public --add-port=3306/tcp --permanent
### 2.搭建gcc环境（gcc是编程语言译器）
yum -y install gcc
yum -y install gcc-c++
### 3.安装需要的软件包
yum install -y yum-utils

### 4.安装镜像仓库
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

### 5.更新yum软件包索引
yum makecache fast

### 6.安装docker引擎
yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
### 7.启动docker
systemctl start docker
### 8.docker设置下anliyun地址
设置阿里云地址
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://xiirc0e4.mirror.aliyuncs.com"]
}
EOF
systemctl daemon-reload
systemctl restart docker
### 9.拉取镜像
docker pull java:17
docker pull mysql:8.0.19
docker pull redis:6.0.8
docker pull zookeeper:3.5.7

![image](https://github.com/user-attachments/assets/19413142-7c0b-42ed-a214-6394d408c55c)

### 10.编写Dockerfile文件，构建镜像
我这里是在根目录下创建了一个mydata目录
在mydata目录下创建Dockerfile文件，编写Dockersfile文件
一个目录下只能有一个Dockerfile文件。

![image](https://github.com/user-attachments/assets/8319b770-e8f5-4c05-a193-1130956281b7)
### 11.进入mydata目录构建镜像
docker build -t blog:1.0 .
### 12.编写docker-compose.yml文件
需要注意的是数据卷积挂载在compose有bug他不能识别文件例如xxx.conf他会弄一个xxx.conf的文件夹。
还需要加入网络用于容器间的通信

![image](https://github.com/user-attachments/assets/81315e26-8fb6-4e7a-b6f3-d7ad32e13663)
![image](https://github.com/user-attachments/assets/98be833a-a77e-4e97-934a-9be86ddce0fb)
执行docker-compose up -d
### 12.遇到报错解决
Docker 无法正确设置 Iptables
sudo iptables -t nat -F
sudo iptables -t filter -F
sudo iptables -t nat -A POSTROUTING -s 172.16.0.0/12 -j MASQUERADE
sudo service docker restart
因为MySQL和Redis还未配置，会出现容器挂掉的情况
将sql文件传到/app/mysql/db
重启redis和mysql解决问题













