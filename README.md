# StartFromFCM_plugin
Ściągnij cały projekt pluginu z githuba 
Rozpakuj go


0. instalacja plugman z npm: npm install -g plugman

1. instalacja pluginu komendą : plugman install --platform android --project <scieżka do folderu \platoforms\android w projekcie do którego ma być wgrany plugin> --plugin <ścieżka do folderu z pluginem>
2. Przykład : plugman install --platform android --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android --plugin C:\Users\Jan.Mazurek\plugins\

5. Należy edytować plik index.js (\platforms\android\app\src\main\assets\www\js) dodając na początku funkcji onDeviceReady() : 
```javascript
		SmartRequestPlugin.ready();
		SmartRequestPlugin.getRequest('<URL>',(t)=> {console.log(t);},(e)=> {console.log(e);});
```
6. <URL> tu wpisz adres strony 
7. Powinno działać
