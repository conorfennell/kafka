# Kafka

```sh
docker-compose up
```
* [kafka-ui](http://localhost:8123/ui)
* [zookeeper-ui](http://localhost:9123) `docker.for.mac.localhost:2181`


// topic called users.
- set the number of partitions 3
- replication factor 2
- topic retention by time
- topic rentention by bytes
  // 3 brokers - brokers bootstrap
  // /topic/users/partition/0/broker-1
  // /topic/users/partition/1/broker-2
  // /topic/users/partition/2/broker-1

users
0 |[][][][][][x][][][][]      - offset 9 - user 1 - 20:20
1 |[][][][]                   - offset 3
2 |[][][][][]                 - offset 4
3 |
4 |
5 |                           - user 1 - 20:21

// consumer starts up -- bootstrap-brokers - topic - earliest | latest - elastic-search-user-consumer
elastic-search-user-consumer
consumer-1 - one partition assigned [0, 3, 5] - [9] - processing 4 records -
consumer-2 - two partitions assigned [1 2, 4] - [5, 4]

// consumer starts up -- bootstrap-brokers - topic - earliest | latest - postgres-user-consumer
elastic-search-user-consumer
consumer-1 - one partition assigned [0] - [9] - processing 4 records -
consumer-2 - two partitions assigned [1 2] - [5, 4]


// key = null value = { "id": "123" , "name": "Conor", "timestamp": "2012-02-12T12:12:12"} -> serilaization -> []bytes [compression algorithim] -> partition round robin
