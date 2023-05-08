REM Remember to replace you student id in variables.tf

terraform init
terraform plan -out main.tfplan
terraform apply main.tfplan

$acrUrl = terraform output --raw acrUrl
$acrUsr = terraform output --raw acrUsr
$acrPwd = terraform output --raw acrPwd

echo "docker login $acrUrl -u $acrUsr -p $acrPwd" >> cmdout.txt
echo "" >> cmdout.txt
echo "cd .\event" >> cmdout.txt
echo "mvn clean package" >> cmdout.txt
echo "docker image build -t $acrUrl/eventsvc ." >> cmdout.txt
echo "docker push $acrUrl/eventsvc" >> cmdout.txt
echo "" >> cmdout.txt
echo "cd ..\ticket" >> cmdout.txt
echo "mvn clean package" >> cmdout.txt
echo "docker image build -t $acrUrl/ticketsvc ." >> cmdout.txt
echo "docker push $acrUrl/ticketsvc" >> cmdout.txt
echo "" >> cmdout.txt
echo "cd ..\venue" >> cmdout.txt
echo "mvn clean package" >> cmdout.txt
echo "docker image build -t $acrUrl/venuesvc ." >> cmdout.txt
echo "docker push $acrUrl/venuesvc" >> cmdout.txt
echo "" >> cmdout.txt
echo "cd ..\webportal ">> cmdout.txt
echo "mvn clean package" >> cmdout.txt
echo "docker image build -t $acrUrl/webportal ." >> cmdout.txt
echo "docker push $acrUrl/webportal" >> cmdout.txt
echo "" >> cmdout.txt

REM terraform plan -destroy -out main.destroy.tfplan
REM terraform apply main.destroy.tfplan