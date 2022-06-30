# Functions List

## Show Prometheus Endpoint
1. pom.xml 
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <scope>runtime</scope>
</dependency>
```
2. configure applicaiton.yaml, include prometheus 
3. show metrics formated for prometheus
http://127.0.0.1:8080/actuator/prometheus


## add prometheus


```
docker run \
    --name prometheus \
    --network host \
    -v /vagrant/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
    -d \
    prom/prometheus
```

visit: 
http://192.168.33.10:9090/targets  
http://192.168.33.10:9090/


## add Grafana
```
docker run --name grafana -d -p 3000:3000 grafana/grafana
```
http://192.168.33.10:3000/