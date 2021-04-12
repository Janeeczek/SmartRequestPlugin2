# StartFromFCM_plugin
Ściągnij cały projekt pluginu z githuba https://github.com/Janeeczek/StartFromFCM_plugin/archive/refs/heads/master.zip 
Rozpakuj go


0. instalacja plugman z npm: npm install -g plugman

1. instalacja pluginu komendą : plugman install --platform android --project <scieżka do folderu \platoforms\android w projekcie do którego ma być wgrany plugin> --plugin <ścieżka do folderu z pluginem>
1.a Przykład : plugman install --platform android --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android --plugin C:\Users\Jan.Mazurek\plugins\autostart

2. Należy wgrać plik google-services.json do folderu \app projektu aplikacji np. C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android\app
3. Należy edytować plik MyFirebaseMessagingService.java (\platforms\android\app\src\main\java\com\example\cordova\plugin\FCM) importując w nim klasę MainActivity zależnie od pakietu aplikacji. (Wiecej info po otworzeniu tego pliku)
3.a Należy edytować plik index.js (\platforms\android\app\src\main\assets\www\js) dodając na początku funkcji onDeviceReady() : 
```javascript
      window.plugins.autostart.show('test', 'long', function() {
          console.log('test!');
        }, function(err) {
          console.log(' ' + err);
        });
```

4. Należy uruchomić aplikacje i sprawdzić logi podczas *run*, aby wyłapać token dla firebase. przykładowy token: f0eJSQcKTsytOOxY2o7egN:APA91bGdAqw1jEsfda8NAWBHFuA00iJ1DEyPS5lRzzf1vECZV1CqOisS-Ri_m_ikS52kpMwiKKmG1KQVMX-pp40eUK-501_Bqe3YaLvtrRM3AYdAQ71iSJVsWVB0qVNbO9v0o8NXVeaW
4.a Token generuje się tylko podczas pierwszego uruchomienia po ponowym zainstalowaniu aplikcji. Tak więc podczas testów wystarczy go sprawdzić tylko raz
4.b W apce zaakceptować pozwolenia i uruchomić ją ponownie
5.a Należy uruchomić https://www.postman.com/ wersję przeglądarkową
5.b Stworzyć nowy request
5.c W Headers dodać parametr key = Authorization | value = key={Server key ze strony projektu firebase}
5.cc przykładowy value = key=AAAASCEBOlU:APA91bEiuZNuU4pW1Tqa04RWs5vsv7dbgBoojpjRLjZwuymOOGjy8cWNNMzjl8k20Cr296PNsdEtobY-w0VQxko2qfymdluNvz4RDekLL4nSf7HD-IhRGEwLNAJfKVH-A16OO7kh59OB
5.ccc nie zapomnij przed wartościa server key dodać *key=*!!!!!
5.d W Body , podzakładka *raw* wkleić :
{
"to" : "token",
"collapse_key" : "type_a",
"data" : {
"START" : "sta",
"title": "Title : Data"
}
}
5.e przykładowy raw:
{
"to" : "f0eJSQcKTsytOOxY2o7egN:APA91bGdAqw1jEsfda8NAWBHFuA00iJ1DEyPS5lRzzf1vECZV1CqOisS-Ri_m_ikS52kpMwiKKmG1KQVMX-pp40eUK-501_Bqe3YaLvtrRM3AYdAQ71iSJVsWVB0qVNbO9v0o8NXVeaW",
"collapse_key" : "type_a",
"data" : {
"START" : "sta",
"title": "Title : Data"
}
}




6. teraz można wyłączyć apke i w PostManie wcisnąc SEND
Aplikacja powinna się sama uruchomić
