REM Remember to **GLOBAL REPLACE** your student id
REM Remember to change the db password and rabbitmq password in 03.comps368tma04-srt.yaml
REM Use the website https://www.base64encode.org/

az aks get-credentials --resource-group "comps368-tma04" --name "s1039518-comps368-tma04"

kubectl apply -f .

kubectl get service venuesvc -n comps368tma04
kubectl get service eventsvc -n comps368tma04
kubectl get service ticketsvc -n comps368tma04
kubectl get service webportal -n comps368tma04
kubectl get service rabbitmq-admin -n comps368tma04

kubectl get pods -n comps368tma04

REM kubectl delete -f .