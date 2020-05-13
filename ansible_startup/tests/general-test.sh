checkStatus=$(curl http://localhost:8080/api/v1/healthCheck)
checkStatus2=$(sudo docker ps | grep -c LogAnalytics)

if [[ -z "$checkStatus" || $checkStatus2 -eq 0 ]]; then
    echo "Service is unavailable!"
else
    echo "Service is up and running!"
fi