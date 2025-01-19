## KANCELARIA PRAWNICZA ,
- Zatrudnia prawników prowadzących różne sprawy. Każda sprawa wymaga skompletowania odpowiednich
dokumentów do sądu 
- Informacje o sprawach zamkniętych prowadzonych przez poszczególnych prawników;
- Informacje o sprawach aktualnie prowadzonych przez poszczególnych prawników
- Informacje o dokumentach złożonych przez klienta w sprawie
- Informacje o dokumentach potrzebnych w sprawie


Documentation Link: [ http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
#### Pro
```shell
  docker run -d --name prometheus -p 9090:9090 -v $(pwd)/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

#### Grafana
```shell
  docker run -d --name grafana -p 9091:3000 grafana/grafana
```
Zaloguj się na http://localhost:3000 (domyślny login: admin, hasło: admin).

