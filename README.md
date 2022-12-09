# Prerequisites
Before running the API start Docker.

# Start service
To start the application with a built-in database:

From root:
```
mvn spring-boot:run -Dspring-boot.run.profiles=database
```

If you want to get your database from Docker Compose: 
```
docker compose up
mvn spring-boot:run
```

The service will initialize the database tables and start up.

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

# To post the connector to Debezium:
```
curl -X POST -H "Content-Type: application/json" -d @connector.json http://localhost:8083/connectors/
```

Debezium will now be observing any changes on your database and will post them on Kafka, on the _r2dbc topic.
To see the messages, navigate to [Kafka UI](http://localhost:9000)

Note that you need to start up Docker Compose in order to have the necessary services in place for this.
