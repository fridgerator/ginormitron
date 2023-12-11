#!/usr/bin/env bash

if [ ! -d signoz ]; then
    git clone -b main https://github.com/SigNoz/signoz.git
fi

echo "docker compse up -d"
docker compose up -d &

echo "sleeping..."
sleep 20

echo "starting services"
# (cd services/admin && ./start_up.sh) &
# (cd services/customerdata && ./start_up.sh) &
# (cd services/retailerdata && ./start_up.sh) &
# (cd services/transactiondata && ./start_up.sh) &

wait
