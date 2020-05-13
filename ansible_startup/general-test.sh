checkStatus=$(curl http://localhost:8080/calc/history)
checkStatus2=$(sudo docker ps | grep -c goApp)

if [[ -z "$checkStatus" || $checkStatus2 -eq 0 ]]; then
    echo "Service is unavailable!"
else
    echo "Service is up and running!"
fi