# ♞ springer

server for `tschess`

##relational 

```
sudo apt update
java -version
sudo apt install default-jre
sudo apt install openjdk-11-jre-headless
sudo apt install openjdk-8-jre-headless
sudo apt install openjdk-9-jre-headless
sudo apt install postgresql postgresql-contrib
sudo apt-get install tmux
sudo apt install gradle
```

## start

run with the following command: 

```
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

### grafana

```
```

### influxDB

```
```