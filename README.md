# ♞ springer

server for `tschess`


##cmd
MM-DD
```
java -jar ./build/libs/springer-1.0.0.jar "--spring.config.location=file:////home/ubuntu/resources/application.properties" "--source=03-28"
```

##relational 

```
sudo apt update
java -version
sudo apt install -y default-jre

sudo apt install -y openjdk-8-jre-headless

sudo apt install -y postgresql postgresql-contrib
sudo apt-get install -y tmux
sudo apt install -y gradle
```

https://www.techrepublic.com/article/how-to-install-fail2ban-on-ubuntu-server-18-04/
```
sudo apt-get install -y fail2ban
sudo systemctl start fail2ban
sudo systemctl enable fail2ban
```
## start

run with the following command: 

```
ssh -o 'StrictHostKeyChecking no' -i "x.pem" ubuntu@127.0.0.1 
git fetch origin master && \
git reset --hard origin/master && \
./gradlew clean build && \
java -jar ./build/libs/springer-1.0.0.jar 
```

## db

list databases
```
`\l` 
```

### create

```
sudo adduser USERNAME --force-badname
```

---

```
psql -U USERNAME
sudo -u postgres psql
sudo -u USERNAME psql
sudo -u USERNAME psql
```

```
CREATE DATABASE DATABASE;
ALTER USER USERNAME WITH PASSWORD PASSWORD;
ALTER ROLE USERNAME WITH SUPERUSER;
CREATE USER USERNAME WITH PASSWORD PASSWORD;
ALTER USER USERNAME WITH PASSWORD PASSWORD;
```

### config

```
\du

SHOW hba_file;

sudo nano /etc/postgresql/10/main/pg_hba.conf

SHOW config_file;

sudo nano /etc/postgresql/10/main/postgresql.conf
```

### test

login:

```
psql -U USERNAME -h localhost
```

facilitate remote connection from dbeaver, add the following line to `postgresql.conf`

```
listen_addresses = '*'
```

add the following as the first line of `pg_hba.conf`

```
# TYPE DATABASE USER CIDR-ADDRESS  METHOD
host   all      all  0.0.0.0/0     trust
```

restart:

 ```
sudo service postgresql stop
sudo service postgresql start
sudo service postgresql restart
 ```

- curl

```
psql -h localhost
curl --header "Content-Type: application/json" --request POST --data '' http://localhost:8080/xxx/xxx
```

```
curl --header "Content-Type: application/json" --request POST --data '{"state": [["x"],["x"],["x"]]}' http://localhost:8080/game/test
```

### tmux

```
tmux new -s tschess
tmux a -t tschess
tmux ls
```

----

##time-series 

####setup:

on ubuntu, as `root`, to assume the role of `root` on AWS you can type the command: 
```
sudo -i
```

### influxDB

install like this:

```
curl -sL https://repos.influxdata.com/influxdb.key | apt-key add -
source /etc/lsb-release
echo "deb https://repos.influxdata.com/${DISTRIB_ID,,} ${DISTRIB_CODENAME} stable" | tee /etc/apt/sources.list.d/influxdb.list
apt-get update && apt-get install influxdb
```

modify the `collectd` influx configuration, make sure the `typesdb` option is pointing to the `collectd` types.

search and modify the `[[collectd]]` block:

```
$  vi /etc/influxdb/influxdb.conf
  
[[collectd]]
  enabled = true
  bind-address = ":25826" # the bind address
  database = "collectd" # Name of the database that will be written to
  retention-policy = ""
  batch-size = 5000 # will flush if this many points get buffered
  batch-pending = 10 # number of batches that may be pending in memory
  batch-timeout = "10s"
  read-buffer = 0 # UDP read buffer size, 0 means to use OS default
  typesdb = "/opt/collectd/share/collectd/types.db"
  security-level = "none" # "none", "sign", or "encrypt"
```

if `collectd` is not installed on the system - download the types.db file as influx needs this
```
$ mkdir /opt/collectd && mkdir /opt/collectd/share && mkdir /opt/collectd/share/collectd && cd /opt/collectd/share/collectd && wget https://raw.githubusercontent.com/collectd/collectd/master/src/types.db && cd -
```

#### start InfluxDB
``` 
$ service influxdb start
```
####create a collectd database
``` 
$ influx
CREATE DATABASE collectd
```

### grafana
```
sudo apt-get install -y apt-transport-https
sudo apt-get install -y software-properties-common wget
wget -q -O - https://packages.grafana.com/gpg.key | sudo apt-key add -

# Alternatively you can add the beta repository, see in the table above
sudo add-apt-repository "deb https://packages.grafana.com/oss/deb stable main"

sudo apt-get update
sudo apt-get install grafana
```

``` 
sudo systemctl start grafana-server
sudo systemctl status grafana-server
sudo systemctl daemon-reload
```

`http://3.12.121.89:3000/`

configure Grafana repos and install it:


Then use a web browser to connect to grafana (http://<serverip>:3000/), using the hostname or IP of your Ubuntu server and port 3000. Log in with admin/admin

 

After logging in. click on Data Sources in the left menu, and then on Add New in the top menu to add a new datasource.

Choose the following options and click Add. Note: If you’re using Grafana 3.0, the Type will just be “InfluxDB”

```
Name: collectd
Type: InfluxDB
Url: http://localhost:8086/
Database: collectd
User: admin
Password: admin
```


###resources

installation

* https://wiki.opnfv.org/display/fastpath/Installing+and+configuring+InfluxDB+and+Grafana+to+display+metrics+with+collectd

writing data to influx

* https://docs.influxdata.com/influxdb/v1.7/guides/writing_data/
* https://docs.influxdata.com/influxdb/v1.7/guides/querying_data/
* https://docs.influxdata.com/influxdb/v1.7/query_language/schema_exploration/
* https://docs.influxdata.com/influxdb/v1.7/tools/shell/
* https://docs.influxdata.com/influxdb/v1.7/troubleshooting/frequently-asked-questions/#how-does-influxdb-handle-field-type-discrepancies-across-shards
* https://docs.influxdata.com/influxdb/v1.7/write_protocols/line_protocol_tutorial/
* https://docs.influxdata.com/influxdb/v1.7/administration/authentication_and_authorization/#grant-administrative-privileges-to-an-existing-user


####misc. commands: 

``` 
influx
use tschess
show measurements
show databases
SHOW FIELD KEYS
CREATE USER sme WITH PASSWORD 111111 WITH ALL PRIVILEGES
```

`SELECT count("player") FROM "activity" GROUP BY time(10m)`

`SELECT count(*) FROM "growth" WHERE "client" = 'ios' GROUP BY time(10m)`
`SELECT count(*) FROM "growth" WHERE "client" = 'android' GROUP BY time(10m)`

`DROP MEASUREMENT`

```
curl -i -XPOST 'http://X.X.X.X:8086/write?db=tschess' --data-binary 'activity player=888,route="login"' 
nano /etc/influxdb/influxdb.conf
netstat -an | grep "LISTEN "
sudo kill -9 `sudo lsof -t -i:8086`
influxd -config /etc/influxdb/influxdb.conf
```