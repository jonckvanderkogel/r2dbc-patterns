# Prerequisites
Before running the API start Docker.

# Start API
From root:
```
mvn spring-boot:run
```

This now downloads the Postgres image, initializes the database tables and starts the API

# Create some data
```
# Create cities
curl -H "Content-Type: application/json" -X POST -d '{"name":"Utrecht"}' http://localhost:8080/city
curl -H "Content-Type: application/json" -X POST -d '{"name":"Amsterdam"}' http://localhost:8080/city

# Create bars
curl -i -H "Content-Type: application/json" -X POST -d '{"name":"Frontaal Bar", "cityId": 1}' http://localhost:8080/bar
curl -i -H "Content-Type: application/json" -X POST -d '{"name":"Brewpub De Kromme Haring", "cityId": 1}' http://localhost:8080/bar
curl -i -H "Content-Type: application/json" -X POST -d '{"name":"vandeStreek Bier", "cityId": 1}' http://localhost:8080/bar
curl -i -H "Content-Type: application/json" -X POST -d '{"name":"Hoekenrode", "cityId": 2}' http://localhost:8080/bar

# Create beers
curl -H "Content-Type: application/json" -X POST -d '{"name":"Juice Punch", "style":"IPA"}' http://localhost:8080/beer
curl -H "Content-Type: application/json" -X POST -d '{"name":"Andreas", "style":"Triple"}' http://localhost:8080/beer
curl -H "Content-Type: application/json" -X POST -d '{"name":"Kia Ora", "style":"IPA"}' http://localhost:8080/beer
curl -H "Content-Type: application/json" -X POST -d '{"name":"Opah", "style":"IPA"}' http://localhost:8080/beer
curl -H "Content-Type: application/json" -X POST -d '{"name":"Mudskipper", "style":"IPA"}' http://localhost:8080/beer
curl -H "Content-Type: application/json" -X POST -d '{"name":"Cuttlefish Idaho 7", "style":"IPA"}' http://localhost:8080/beer
curl -H "Content-Type: application/json" -X POST -d '{"name":"Erdinger", "style":"WEIZEN"}' http://localhost:8080/beer

# Associate beers with bars
curl -H "Content-Type: application/json" -X POST -d '{"barId":1, "beerId": 1}' http://localhost:8080/barbeer
curl -H "Content-Type: application/json" -X POST -d '{"barId":1, "beerId": 2}' http://localhost:8080/barbeer
curl -H "Content-Type: application/json" -X POST -d '{"barId":1, "beerId": 3}' http://localhost:8080/barbeer
curl -H "Content-Type: application/json" -X POST -d '{"barId":2, "beerId": 4}' http://localhost:8080/barbeer
curl -H "Content-Type: application/json" -X POST -d '{"barId":2, "beerId": 5}' http://localhost:8080/barbeer
curl -H "Content-Type: application/json" -X POST -d '{"barId":2, "beerId": 6}' http://localhost:8080/barbeer
curl -H "Content-Type: application/json" -X POST -d '{"barId":4, "beerId": 7}' http://localhost:8080/barbeer

# Get city 1
curl "http://localhost:8080/city?id=1"

# Update bar
curl -H "Content-Type: application/json" -X PUT -d '{"id": 1, "name":"Frontaal Bar", "cityId": 2}' http://localhost:8080/bar
```
