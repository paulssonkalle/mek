#!/bin/bash

docker-compose exec mongodb01 /usr/bin/mongo -u root -p root --eval '''if (rs.status()["ok"] == 0) {
    rsconf = {
      _id : "rs0",
      members: [
        { _id : 0, host : "mongodb01:27017", priority: 1.0 },
        { _id : 1, host : "mongodb02:27017", priority: 0.5 },
        { _id : 2, host : "mongodb03:27017", priority: 0.5 }
      ]
    };
    rs.initiate(rsconf);
}
rs.conf();'''